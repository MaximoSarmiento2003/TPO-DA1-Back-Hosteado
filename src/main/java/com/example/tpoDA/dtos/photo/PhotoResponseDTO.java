package com.example.tpoDA.dtos.photo;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoResponseDTO {

    private Integer id;
    private Integer productId;
    private String imageBase64;
}
