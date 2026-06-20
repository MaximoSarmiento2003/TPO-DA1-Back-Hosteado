package com.example.tpoDA.dtos.sector;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectorCreateDTO {

    private String name;
    private String code;
    private Integer responsibleId;
}
