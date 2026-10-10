package br.com.fiap.inovaflow_backend.dto;

public record IdeiaRequest(
        String titulo,
        String descricao,
        String categoria,
        String estrategiaId
) {
}