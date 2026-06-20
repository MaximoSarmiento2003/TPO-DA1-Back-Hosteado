package com.example.tpoDA.dtos.sector;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectorResponseDTO {

    private Integer id;
    private String name;
    private String code;

    private Integer responsibleId;
    private String responsiblePosition;
}
