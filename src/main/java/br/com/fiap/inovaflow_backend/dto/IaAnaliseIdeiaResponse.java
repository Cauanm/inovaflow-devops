package br.com.fiap.inovaflow_backend.dto;

import br.com.fiap.inovaflow_backend.model.PrioridadeIdeia;

import java.time.LocalDateTime;

public record IaAnaliseIdeiaResponse(
        String ideiaId,
        Integer pontuacao,
        PrioridadeIdeia prioridadeSugerida,
        String justificativa,
        LocalDateTime dataAnalise
) {
}