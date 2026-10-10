package br.com.fiap.inovaflow_backend.dto;

import br.com.fiap.inovaflow_backend.model.StatusEstrategia;

public record EstrategiaRequest(
        String titulo,
        String descricao,
        String categoria,
        String campanha,
        StatusEstrategia status
) {
}