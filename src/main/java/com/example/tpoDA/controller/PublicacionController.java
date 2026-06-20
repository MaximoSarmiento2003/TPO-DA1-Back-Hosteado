package com.example.tpoDA.controller;

import com.example.tpoDA.dtos.publicacion.PublicacionCreateDTO;
import com.example.tpoDA.dtos.publicacion.PublicacionResponseDTO;
import com.example.tpoDA.services.PublicacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/publicaciones")
@RequiredArgsConstructor
public class PublicacionController {

    private final PublicacionService publicacionService;

    // =========================
    // POST /publicaciones
    // Endpoint 21 - Subir artículo para revisión
    // =========================
    @PostMapping
    public ResponseEntity<Map<String, Object>> create(
            @RequestBody PublicacionCreateDTO dto,
            Authentication auth
    ) {
        PublicacionResponseDTO response = publicacionService.create(dto, auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "message", "Artículo enviado para revisión.",
                        "publicacionId", response.getId(),
                        "estado", "PENDIENTE_REVISION"
                ));
    }

    // =========================
    // POST /publicaciones/{publicacionId}/imagenes
    // Endpoint 22 - Subir imágenes del artículo
    // =========================
    @PostMapping("/{publicacionId}/imagenes")
    public ResponseEntity<Map<String, Object>> uploadImages(
            @PathVariable Integer publicacionId,
            @RequestParam("files") List<MultipartFile> files,
            Authentication auth
    ) {
        // Por ahora se reciben las imágenes y se confirma la cantidad
        // La lógica de persistencia de imágenes se puede extender aquí
        return ResponseEntity.ok(Map.of(
                "message", "Imágenes cargadas correctamente.",
                "cantidad", files.size()
        ));
    }

    // =========================
    // GET /publicaciones/mis-publicaciones
    // Endpoint 23 - Ver publicaciones del usuario
    // =========================
    @GetMapping("/mis-publicaciones")
    public ResponseEntity<List<PublicacionResponseDTO>> getMisPublicaciones(Authentication auth) {
        return ResponseEntity.ok(publicacionService.getMisPublicaciones(auth.getName()));
    }
}