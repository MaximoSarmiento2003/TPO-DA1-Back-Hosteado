package com.example.tpoDA.mappers;

import com.example.tpoDA.dtos.owner.OwnerCreateDTO;
import com.example.tpoDA.dtos.owner.OwnerResponseDTO;
import com.example.tpoDA.dtos.owner.OwnerUpdateDTO;
import com.example.tpoDA.entities.Country;
import com.example.tpoDA.entities.Employee;
import com.example.tpoDA.entities.Owner;
import com.example.tpoDA.entities.Person;

public class OwnerMapper {

    public static Owner toEntity(OwnerCreateDTO dto, Person person, Country country, Employee verifier) {
        if (dto == null || person == null || verifier == null) return null;

        return Owner.builder()
                .person(person)
                .country(country)
                .verifier(verifier)
                .financialVerification(toDB(dto.getFinancialVerification()))
                .judicialVerification(toDB(dto.getJudicialVerification()))
                .riskRating(dto.getRiskRating())
                .build();
    }

    public static OwnerResponseDTO toDTO(Owner entity) {
        if (entity == null) return null;

        OwnerResponseDTO dto = new OwnerResponseDTO();

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

        dto.setFinancialVerification(toBoolean(entity.getFinancialVerification()));
        dto.setJudicialVerification(toBoolean(entity.getJudicialVerification()));

        dto.setRiskRating(entity.getRiskRating());

        return dto;
    }

    public static void updateEntity(Owner entity, OwnerUpdateDTO dto, Country country, Employee verifier) {
        if (entity == null || dto == null) return;

        if (country != null) {
            entity.setCountry(country);
        }

        if (verifier != null) {
            entity.setVerifier(verifier);
        }

        if (dto.getFinancialVerification() != null) {
            entity.setFinancialVerification(toDB(dto.getFinancialVerification()));
        }

        if (dto.getJudicialVerification() != null) {
            entity.setJudicialVerification(toDB(dto.getJudicialVerification()));
        }

        if (dto.getRiskRating() != null) {
            entity.setRiskRating(dto.getRiskRating());
        }
    }

    // 🔽 Boolean ↔ DB

    private static String toDB(Boolean value) {
        if (value == null) return null;
        return value ? "si" : "no";
    }

    private static Boolean toBoolean(String value) {
        if (value == null) return null;
        return value.equalsIgnoreCase("si");
    }
}
