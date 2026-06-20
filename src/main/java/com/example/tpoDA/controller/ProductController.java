package com.example.tpoDA.controller;

import com.example.tpoDA.dtos.product.ProductCreateDTO;
import com.example.tpoDA.dtos.product.ProductDetailResponseDTO;
import com.example.tpoDA.dtos.product.ProductResponseDTO;
import com.example.tpoDA.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(
            @RequestBody ProductCreateDTO dto,
            Authentication auth
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.createProduct(auth.getName(), dto));
    }

    // Solo devuelve los aprobados (available = 'si')
    @GetMapping("/mine")
    public ResponseEntity<List<ProductResponseDTO>> getMine(Authentication auth) {
        return ResponseEntity.ok(productService.getMyApprovedProducts(auth.getName()));
    }

    // GET /products/{id}/detail — detalle completo con fotos, seguro y depósito
    @GetMapping("/{id}/detail")
    public ResponseEntity<ProductDetailResponseDTO> getDetail(
            @PathVariable Integer id,
            Authentication auth
    ) {
        return ResponseEntity.ok(productService.getProductDetail(auth.getName(), id));
    }
}
