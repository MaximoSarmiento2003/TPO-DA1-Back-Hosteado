package com.example.tpoDA.mappers;

import com.example.tpoDA.dtos.client.ClientCreateDTO;
import com.example.tpoDA.dtos.client.ClientResponseDTO;
import com.example.tpoDA.dtos.client.ClientUpdateDTO;
import com.example.tpoDA.entities.Client;
import com.example.tpoDA.entities.ClientCategory;
import com.example.tpoDA.entities.Country;
import com.example.tpoDA.entities.Employee;
import com.example.tpoDA.entities.Person;

public class ClientMapper {

    public static Client toEntity(ClientCreateDTO dto, Person person, Country country, Employee verifier) {
        if (dto == null || person == null || verifier == null) return null;

        return Client.builder()
                .person(person)
                .country(country)
                .verifier(verifier)
                .admitted(toDB(dto.getAdmitted()))
                .category(toCategory(dto.getCategory()))
                .build();
    }

    public static ClientResponseDTO toDTO(Client entity) {
        if (entity == null) return null;

        ClientResponseDTO dto = new ClientResponseDTO();

        dto.setId(entity.getId());

        if (entity.getPerson() != null) {
            dto.setName(entity.getPerson().getName());
            dto.setDocument(entity.getPerson().getDocument());
        }

        if (entity.getCountry() != null) {
            dto.setCountryName(entity.getCountry().getName());
        }

        if (entity.getVerifier() != null) {
            dto.setVerifierId(entity.getVerifier().getId());
        }

        dto.setAdmitted(toBoolean(entity.getAdmitted()));
        dto.setCategory(fromCategory(entity.getCategory()));

        return dto;
    }

    public static void updateEntity(Client entity, ClientUpdateDTO dto, Country country, Employee verifier) {
        if (entity == null || dto == null) return;

        if (country != null) {
            entity.setCountry(country);
        }

        if (verifier != null) {
            entity.setVerifier(verifier);
        }

        if (dto.getAdmitted() != null) {
            entity.setAdmitted(toDB(dto.getAdmitted()));
        }

        if (dto.getCategory() != null) {
            entity.setCategory(toCategory(dto.getCategory()));
        }
    }

    //Boolean ↔ DB

    private static String toDB(Boolean value) {
        if (value == null) return null;
        return value ? "si" : "no";
    }

    private static Boolean toBoolean(String value) {
        if (value == null) return null;
        return value.equalsIgnoreCase("si");
    }

    //Category ↔ Enum

    private static ClientCategory toCategory(String value) {
        if (value == null) return null;

        return switch (value.toLowerCase()) {
            case "comun" -> ClientCategory.COMUN;
            case "especial" -> ClientCategory.ESPECIAL;
            case "plata" -> ClientCategory.PLATA;
            case "oro" -> ClientCategory.ORO;
            case "platino" -> ClientCategory.PLATINO;
            default -> throw new IllegalArgumentException("Categoría inválida: " + value);
        };
    }

    private static String fromCategory(ClientCategory category) {
        if (category == null) return null;

        return category.name().toLowerCase();
    }
}
