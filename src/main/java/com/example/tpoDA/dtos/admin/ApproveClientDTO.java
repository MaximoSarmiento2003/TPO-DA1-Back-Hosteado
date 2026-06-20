package com.example.tpoDA.dtos.admin;

import com.example.tpoDA.entities.ClientCategory;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApproveClientDTO {
    // Categoría que se le asigna al aprobar (default COMUN si no se envía)
    private ClientCategory category;
}
