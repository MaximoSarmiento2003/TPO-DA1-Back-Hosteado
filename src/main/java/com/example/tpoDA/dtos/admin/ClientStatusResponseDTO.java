package com.example.tpoDA.dtos.admin;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientStatusResponseDTO {
    private Integer clientId;
    private String email;
    private String name;
    private String registrationStatus;
    private String admitted;
    private String category;
}
