package br.com.fiap.inovaflow_backend.dto;

import br.com.fiap.inovaflow_backend.model.StatusEstrategia;

import java.time.LocalDateTime;

public record EstrategiaResponse(
        String id,
        String titulo,
        String descricao,
        String categoria,
        String campanha,
        StatusEstrategia status,
        LocalDateTime dataCriacao
) {
}