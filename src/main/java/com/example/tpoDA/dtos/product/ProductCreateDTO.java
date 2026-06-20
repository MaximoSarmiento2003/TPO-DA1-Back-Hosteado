package com.example.tpoDA.dtos.product;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateDTO {

    // "normal" | "art"
    private String type;

    // Campos comunes
    private String name;
    private String brand;           // marca / autor
    private Integer quantity;
    private String catalogDescription;   // descripción corta para el catálogo
    private String fullDescription;      // provenance & narrative

    // Solo ArtProduct
    private Integer year;
    private String history;

    // Declaración de propiedad
    private Boolean ownerConfirmed;

    // Fotos en Base64 (mínimo 6 según consigna)
    private List<String> photosBase64;
}
