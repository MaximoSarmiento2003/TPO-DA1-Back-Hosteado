package com.example.tpoDA.services;

import com.example.tpoDA.dtos.login.AuthResponseDTO;
import com.example.tpoDA.dtos.login.LoginRequestDTO;
import com.example.tpoDA.dtos.register.RegisterRequestDTO;
import com.example.tpoDA.dtos.register.RegisterResponseDTO;
import com.example.tpoDA.dtos.register.SetPasswordRequestDTO;
import com.example.tpoDA.entities.*;
import com.example.tpoDA.exceptions.AppException;
import com.example.tpoDA.repositories.*;
import com.example.tpoDA.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final ClientRepository clientRepository;
    private final CountryRepository countryRepository;
    private final EmployeeRepository employeeRepository;
    private final PendingRegistrationRepository pendingRegistrationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final DniPhotoRepository dniPhotoRepository;

    // ─── LOGIN ───────────────────────────────────────────────────────────────

    public AuthResponseDTO login(LoginRequestDTO request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException("Email o contraseña incorrectos", HttpStatus.UNAUTHORIZED));

        if (user.getPassword() == null) {
            throw new AppException("Tu cuenta aún no tiene contraseña. Revisá tu correo para activarla.", HttpStatus.FORBIDDEN);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException("Email o contraseña incorrectos", HttpStatus.UNAUTHORIZED);
        }

        if (user.getRegistrationStatus() == RegistrationStatus.PENDIENTE) {
        throw new AppException(
                "Tu cuenta aún está pendiente de aprobación.",
                HttpStatus.FORBIDDEN
        );
    }

    if (user.getRegistrationStatus() == RegistrationStatus.RECHAZADO) {
        throw new AppException(
                "Tu solicitud de registro fue rechazada.",
                HttpStatus.FORBIDDEN
        );
    }

        String token = jwtService.generateToken(user.getEmail());
        Client client = user.getClient();
        Person person = client.getPerson();

        return AuthResponseDTO.builder()
                .token(token)
                .userId(user.getId())
                .clientId(client.getId())
                .email(user.getEmail())
                .name(person.getName())
                .category(client.getCategory() != null ? client.getCategory().name() : null)
                .admitted(client.getAdmitted() != null && client.getAdmitted().equalsIgnoreCase("si"))
                .build();
    }

    // ─── PASO 1: REGISTRO (sin contraseña) ───────────────────────────────────

    @Transactional
public RegisterResponseDTO register(RegisterRequestDTO request) {

    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
        throw new AppException("El email ya está registrado", HttpStatus.CONFLICT);
    }

    if (personRepository.existsByDocument(request.getDocument())) {
        throw new AppException("El documento ya está registrado", HttpStatus.CONFLICT);
    }

    Country country = null;
    if (request.getCountryId() != null) {
        country = countryRepository.findById(request.getCountryId())
                .orElseThrow(() -> new AppException("País no encontrado", HttpStatus.BAD_REQUEST));
    }

    Employee verifier = employeeRepository.findRandom()
            .orElseThrow(() -> new AppException("No hay empleados disponibles", HttpStatus.INTERNAL_SERVER_ERROR));

    byte[] fotoFrente = decodeBase64Image(request.getDniFrenteBase64(), "DNI frente");
    byte[] fotoDorso  = decodeBase64Image(request.getDniDorsoBase64(),  "DNI dorso");

    // 1. Person — solo guardamos el frente en el campo foto existente
    String fullName = buildFullName(request.getNombre(), request.getApellido());
    Person person = Person.builder()
            .name(fullName)
            .document(request.getDocument())
            .address(request.getAddress())
            .status(PersonStatus.ACTIVO)
            .photo(fotoFrente)
            .build();
    person = personRepository.save(person);

    // 2. DniPhoto — guardamos ambas fotos en nuestra tabla nueva
    DniPhoto dniPhoto = DniPhoto.builder()
            .personId(person.getId())
            .frente(fotoFrente)
            .dorso(fotoDorso)
            .build();
    dniPhotoRepository.save(dniPhoto);

    // 3. Client
    Client client = new Client();
    client.setPerson(person);
    client.setCountry(country);
    client.setVerifier(verifier);
    client.setAdmitted("no");
    client.setCategory(ClientCategory.COMUN);
    client = clientRepository.save(client);

    // 4. User — sin contraseña todavía
    User user = User.builder()
            .client(client)
            .email(request.getEmail())
            .password(null)
            .build();
    user = userRepository.save(user);

    // 5. Token de activación (expira en 48hs)
    String activationToken = UUID.randomUUID().toString();
    PendingRegistration pending = PendingRegistration.builder()
            .user(user)
            .token(activationToken)
            .expiresAt(LocalDateTime.now().plusHours(48))
            .used(false)
            .build();
    pendingRegistrationRepository.save(pending);


    return RegisterResponseDTO.builder()
            .message("Registro recibido. Recibirás un mail cuando tu cuenta sea aprobada.")
            .email(request.getEmail())
            .build();
}

    // ─── PASO 2: SET PASSWORD (desde el link del mail) ────────────────────────

    @Transactional
    public AuthResponseDTO setPassword(SetPasswordRequestDTO request) {

        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new AppException("La contraseña debe tener al menos 6 caracteres", HttpStatus.BAD_REQUEST);
        }

        PendingRegistration pending = pendingRegistrationRepository.findByToken(request.getToken())
                .orElseThrow(() -> new AppException("El link no es válido", HttpStatus.BAD_REQUEST));

        if (pending.isUsed()) {
            throw new AppException("Este link ya fue utilizado", HttpStatus.CONFLICT);
        }

        if (pending.isExpired()) {
            throw new AppException("El link expiró. Contactá al soporte para recibir uno nuevo.", HttpStatus.GONE);
        }

        // Setear contraseña y activar cuenta
        User user = pending.getUser();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        // Marcar token como usado
        pending.setUsed(true);
        pendingRegistrationRepository.save(pending);

        // Devolver JWT para que el usuario quede logueado directamente
        String jwt = jwtService.generateToken(user.getEmail());
        Client client = user.getClient();
        Person person = client.getPerson();

        return AuthResponseDTO.builder()
                .token(jwt)
                .userId(user.getId())
                .clientId(client.getId())
                .email(user.getEmail())
                .name(person.getName())
                .category(client.getCategory() != null ? client.getCategory().name() : null)
                .admitted(client.getAdmitted() != null && client.getAdmitted().equalsIgnoreCase("si"))
                .build();
    }

    // ─── HELPERS ─────────────────────────────────────────────────────────────

    private String buildFullName(String nombre, String apellido) {
        return ((nombre != null ? nombre : "") + " " + (apellido != null ? apellido : "")).trim();
    }

    private byte[] decodeBase64Image(String base64, String fieldName) {
        if (base64 == null || base64.isBlank()) return null;
        try {
            String clean = base64.replaceFirst("^data:image/[^;]+;base64,", "");
            return Base64.getDecoder().decode(clean);
        } catch (IllegalArgumentException e) {
            throw new AppException("La imagen del " + fieldName + " no es válida", HttpStatus.BAD_REQUEST);
        }
    }
}