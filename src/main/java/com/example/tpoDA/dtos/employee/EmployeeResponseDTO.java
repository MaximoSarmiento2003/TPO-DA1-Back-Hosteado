package com.example.tpoDA.dtos.employee;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {

    private Integer id;
    private String position;
    private Integer sector;
}
