package br.com.fiap.inovaflow_backend.dto;

import br.com.fiap.inovaflow_backend.model.StatusProjeto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProjetoResponse(
        String id,
        String nome,
        String descricao,
        String etapa,
        StatusProjeto status,
        Double investimento,
        Double retornoFinanceiro,
        Double lucroObtido,
        Double roi,
        Double aumentoProdutividade,
        LocalDate prazo,
        String estrategiaId,
        String ideiaId,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {
}