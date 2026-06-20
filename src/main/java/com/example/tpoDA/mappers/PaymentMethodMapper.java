package com.example.tpoDA.mappers;

import com.example.tpoDA.dtos.payment.PaymentMethodCreateDTO;
import com.example.tpoDA.dtos.payment.PaymentMethodResponseDTO;
import com.example.tpoDA.entities.PaymentMethod;
import com.example.tpoDA.entities.PaymentMethodTipo;
import com.example.tpoDA.entities.User;

public class PaymentMethodMapper {

    public static PaymentMethod toEntity(PaymentMethodCreateDTO dto, User user) {
        if (dto == null || user == null) return null;

        String ultimos4 = null;
        if (dto.getNumeroTarjeta() != null && dto.getNumeroTarjeta().length() >= 4) {
            ultimos4 = dto.getNumeroTarjeta()
                    .substring(dto.getNumeroTarjeta().length() - 4);
        }

        return PaymentMethod.builder()
                .user(user)
                .tipo(PaymentMethodTipo.valueOf(dto.getTipo()))
                .titular(dto.getTitular())
                .marca(dto.getMarca())
                .ultimos4(ultimos4)
                .fechaExpiracion(dto.getFechaExpiracion())
                .banco(dto.getBanco())
                .cbu(dto.getCbu() != null ? "****" + dto.getCbu().substring(Math.max(0, dto.getCbu().length() - 4)) : null)
                .montoGarantizado(dto.getMontoGarantizado())
                .moneda(dto.getMoneda())
                .verificado(false)
                .estado("PENDIENTE_VERIFICACION")
                .build();
    }

    public static PaymentMethodResponseDTO toDTO(PaymentMethod entity) {
        if (entity == null) return null;

        PaymentMethodResponseDTO dto = new PaymentMethodResponseDTO();
        dto.setId(entity.getId());
        dto.setTipo(entity.getTipo() != null ? entity.getTipo().name() : null);
        dto.setMarca(entity.getMarca());
        dto.setUltimos4(entity.getUltimos4());
        dto.setTitular(entity.getTitular());
        dto.setBanco(entity.getBanco());
        dto.setCbu(entity.getCbu());
        dto.setMontoGarantizado(entity.getMontoGarantizado());
        dto.setMoneda(entity.getMoneda());
        dto.setVerificado(entity.getVerificado());
        return dto;
    }
}
