package com.example.tpoDA.mappers;

import com.example.tpoDA.dtos.insurance.InsuranceCreateDTO;
import com.example.tpoDA.dtos.insurance.InsuranceResponseDTO;
import com.example.tpoDA.dtos.insurance.InsuranceUpdateDTO;
import com.example.tpoDA.entities.Insurance;

public class InsuranceMapper {

    public static Insurance toEntity(InsuranceCreateDTO dto) {
        if (dto == null) return null;

        return Insurance.builder()
                .policyNumber(dto.getPolicyNumber())
                .company(dto.getCompany())
                .combinedPolicy(toDB(dto.getCombinedPolicy()))
                .amount(dto.getAmount())
                .build();
    }

    public static InsuranceResponseDTO toDTO(Insurance entity) {
        if (entity == null) return null;

        return new InsuranceResponseDTO(
                entity.getPolicyNumber(),
                entity.getCompany(),
                toBoolean(entity.getCombinedPolicy()),
                entity.getAmount()
        );
    }

    public static void updateEntity(Insurance entity, InsuranceUpdateDTO dto) {
        if (entity == null || dto == null) return;

        if (dto.getCompany() != null) {
            entity.setCompany(dto.getCompany());
        }

        if (dto.getCombinedPolicy() != null) {
            entity.setCombinedPolicy(toDB(dto.getCombinedPolicy()));
        }

        if (dto.getAmount() != null) {
            entity.setAmount(dto.getAmount());
        }
    }

    //CONVERSIONES CLAVE

    private static String toDB(Boolean value) {
        if (value == null) return null;
        return value ? "si" : "no";
    }

    private static Boolean toBoolean(String value) {
        if (value == null) return null;
        return value.equalsIgnoreCase("si");
    }
}

