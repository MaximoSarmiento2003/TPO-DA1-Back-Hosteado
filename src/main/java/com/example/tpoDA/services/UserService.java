package com.example.tpoDA.services;

import com.example.tpoDA.dtos.user.ChangePasswordDTO;
import com.example.tpoDA.dtos.user.UserProfileResponseDTO;
import com.example.tpoDA.dtos.user.UserProfileUpdateDTO;
import com.example.tpoDA.entities.Client;
import com.example.tpoDA.entities.Country;
import com.example.tpoDA.entities.Person;
import com.example.tpoDA.entities.User;
import com.example.tpoDA.repositories.CountryRepository;
import com.example.tpoDA.repositories.PersonRepository;
import com.example.tpoDA.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final CountryRepository countryRepository;
    private final PasswordEncoder passwordEncoder;

    // =========================
    // GET: obtener perfil
    // =========================
    public UserProfileResponseDTO getProfile(String email) {
        User user = getUserByEmail(email);
        Client client = user.getClient();
        Person person = client.getPerson();

        UserProfileResponseDTO dto = new UserProfileResponseDTO();
        dto.setId(user.getId());
        dto.setNombre(person != null ? person.getName() : null);
        dto.setEmail(user.getEmail());
        dto.setDomicilioLegal(person != null ? person.getAddress() : null);
        dto.setPaisOrigen(client.getCountry() != null ? client.getCountry().getName() : null);
        dto.setCategoria(client.getCategory() != null ? client.getCategory().name() : null);
        dto.setEstado(person != null && person.getStatus() != null ? person.getStatus().name() : null);
        return dto;
    }

    // =========================
    // PUT: actualizar perfil
    // =========================
    public Map<String, String> updateProfile(UserProfileUpdateDTO dto, String email) {
        User user = getUserByEmail(email);
        Client client = user.getClient();
        Person person = client.getPerson();

        // Actualizar domicilio en Person
        if (dto.getDomicilioLegal() != null && person != null) {
            person.setAddress(dto.getDomicilioLegal());
            personRepository.save(person);
        }

        // Actualizar país en Client
        if (dto.getPaisOrigen() != null) {
            Country country = countryRepository.findByNameIgnoreCase(dto.getPaisOrigen())
                    .orElseThrow(() -> new RuntimeException("País no encontrado: " + dto.getPaisOrigen()));
            client.setCountry(country);
            // Client se persiste a través del cascade desde User o directamente
            // Si no hay cascade, necesitás un ClientRepository; usá personRepository como proxy
        }

        return Map.of("message", "Perfil actualizado correctamente");
    }

    // =========================
    // PUT: cambiar contraseña
    // =========================
    public Map<String, String> changePassword(ChangePasswordDTO dto, String email) {
        User user = getUserByEmail(email);

        // Validar contraseña actual
        if (!passwordEncoder.matches(dto.getPasswordActual(), user.getPassword())) {
            throw new RuntimeException("Contraseña actual incorrecta");
        }

        // Validar que nueva == confirmación
        if (!dto.getPasswordNueva().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("Las contraseñas no coinciden");
        }

        // Validar que no sea igual a la anterior
        if (passwordEncoder.matches(dto.getPasswordNueva(), user.getPassword())) {
            throw new RuntimeException("La nueva contraseña no puede ser igual a la anterior");
        }

        user.setPassword(passwordEncoder.encode(dto.getPasswordNueva()));
        userRepository.save(user);

        return Map.of("message", "Contraseña actualizada correctamente");
    }

    // =========================
    // Helper
    // =========================
    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}