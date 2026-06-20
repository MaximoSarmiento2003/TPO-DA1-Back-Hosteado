package com.example.tpoDA.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // =========================
        // HEADER AUTHORIZATION
        // =========================

        final String authHeader =
                request.getHeader("Authorization");

        // Si no hay token
        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        // =========================
        // EXTRAER TOKEN
        // =========================

        final String token =
                authHeader.substring(7);

        // =========================
        // EXTRAER EMAIL
        // =========================

        final String email =
                jwtService.extractUsername(token);

        // =========================
        // SI NO ESTÁ AUTENTICADO
        // =========================

        if (email != null &&
                SecurityContextHolder.getContext()
                        .getAuthentication() == null) {

            // Buscar usuario
            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(email);

            // Validar token
            if (jwtService.isTokenValid(token, userDetails.getUsername())) {

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // =========================
                // AUTENTICAR USUARIO
                // =========================

                SecurityContextHolder.getContext()
                        .setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
