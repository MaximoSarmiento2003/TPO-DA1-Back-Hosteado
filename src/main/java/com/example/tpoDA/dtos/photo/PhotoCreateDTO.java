package com.example.tpoDA.dtos.photo;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoCreateDTO {

    private Integer productId;
    private String imageBase64;
}
