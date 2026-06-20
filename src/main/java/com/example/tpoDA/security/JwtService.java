package com.example.tpoDA.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    //cambiar después
    private static final String SECRET_KEY =
            "miclavesupersecretamiclavesupersecretamiclave";

    private final Key key =
            Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // =========================
    // GENERAR TOKEN
    // =========================

    public String generateToken(String email) {

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())

                // 24 horas
                .setExpiration(
                        new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)
                )

                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // =========================
    // EXTRAER EMAIL
    // =========================

    public String extractUsername(String token) {

        return extractAllClaims(token)
                .getSubject();
    }

    // =========================
    // VALIDAR TOKEN
    // =========================

    public boolean isTokenValid(String token, String email) {

        final String username = extractUsername(token);

        return username.equals(email)
                && !isTokenExpired(token);
    }

    // =========================
    // TOKEN EXPIRADO
    // =========================

    private boolean isTokenExpired(String token) {

        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    // =========================
    // CLAIMS
    // =========================

    private Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
