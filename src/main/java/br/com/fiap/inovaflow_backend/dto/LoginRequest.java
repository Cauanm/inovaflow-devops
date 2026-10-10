package br.com.fiap.inovaflow_backend.dto;

public record LoginRequest(
        String email,
        String senha
) {
}