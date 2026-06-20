package com.example.tpoDA.mappers;

import com.example.tpoDA.dtos.user.UserCreateDTO;
import com.example.tpoDA.dtos.user.UserResponseDTO;
import com.example.tpoDA.dtos.user.UserUpdateDTO;
import com.example.tpoDA.entities.Client;
import com.example.tpoDA.entities.User;

public class UserMapper {

    public static User toEntity(UserCreateDTO dto, Client client) {
        if (dto == null || client == null) return null;

        return User.builder()
                .client(client)
                .email(dto.getEmail())
                .password(dto.getPassword()) // 🔥 encriptar en service
                .build();
    }

    public static UserResponseDTO toDTO(User entity) {
        if (entity == null) return null;

        UserResponseDTO dto = new UserResponseDTO();

        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());

        if (entity.getClient() != null) {
            Client client = entity.getClient();

            dto.setCategory(
                client.getCategory() != null 
                ? client.getCategory().name().toLowerCase()
                : null
            );

            dto.setAdmitted(
                client.getAdmitted() != null &&
                client.getAdmitted().equalsIgnoreCase("si")
            );

            if (client.getPerson() != null) {
                dto.setName(client.getPerson().getName());
                dto.setDocument(client.getPerson().getDocument());
            }
        }

        return dto;
    }

    public static void updateEntity(User entity, UserUpdateDTO dto) {
        if (entity == null || dto == null) return;

        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }

        if (dto.getPassword() != null) {
            entity.setPassword(dto.getPassword()); // 🔥 encriptar en service
        }
    }
}

