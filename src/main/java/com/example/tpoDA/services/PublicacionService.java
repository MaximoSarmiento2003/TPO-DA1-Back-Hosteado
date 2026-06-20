package com.example.tpoDA.services;

import com.example.tpoDA.dtos.publicacion.PublicacionCreateDTO;
import com.example.tpoDA.dtos.publicacion.PublicacionResponseDTO;
import com.example.tpoDA.entities.Client;
import com.example.tpoDA.entities.Publicacion;
import com.example.tpoDA.entities.User;
import com.example.tpoDA.mappers.PublicacionMapper;
import com.example.tpoDA.repositories.PublicacionRepository;
import com.example.tpoDA.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicacionService {

    private final PublicacionRepository publicacionRepository;
    private final UserRepository userRepository;

    // =========================
    // POST: subir artículo para revisión
    // =========================
    public PublicacionResponseDTO create(PublicacionCreateDTO dto, String email) {
        User user = getUserByEmail(email);
        Client client = user.getClient();

        Publicacion entity = PublicacionMapper.toEntity(dto, client);
        return PublicacionMapper.toDTO(publicacionRepository.save(entity));
    }

    // =========================
    // GET: listar publicaciones del usuario autenticado
    // =========================
    public List<PublicacionResponseDTO> getMisPublicaciones(String email) {
        User user = getUserByEmail(email);
        Client client = user.getClient();

        return publicacionRepository.findByClient(client)
                .stream()
                .map(PublicacionMapper::toDTO)
                .collect(Collectors.toList());
    }

    // =========================
    // Helper
    // =========================
    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
