package com.example.tpoDA.controller;

import com.example.tpoDA.repositories.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoRepository photoRepository;

    // Imagen individual por ID
    @GetMapping("/{photoId}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable Integer photoId) {
        return photoRepository.findById(photoId)
                .map(photo -> ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(photo.getImage()))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
