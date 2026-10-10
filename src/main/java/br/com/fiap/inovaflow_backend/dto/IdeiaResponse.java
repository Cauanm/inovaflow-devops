package br.com.fiap.inovaflow_backend.dto;

import br.com.fiap.inovaflow_backend.model.PrioridadeIdeia;
import br.com.fiap.inovaflow_backend.model.StatusIdeia;

import java.time.LocalDateTime;

public record IdeiaResponse(
        String id,
        String titulo,
        String descricao,
        String categoria,
        StatusIdeia status,
        PrioridadeIdeia prioridade,
        String operadorId,
        String operadorNome,
        String estrategiaId,
        String observacaoGestor,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao,
        Integer pontuacaoIa,
        PrioridadeIdeia prioridadeSugeridaIa,
        String justificativaIa
) {
}