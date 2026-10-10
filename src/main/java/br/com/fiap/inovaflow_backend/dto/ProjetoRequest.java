package br.com.fiap.inovaflow_backend.dto;

import br.com.fiap.inovaflow_backend.model.StatusProjeto;

import java.time.LocalDate;

public record ProjetoRequest(
        String nome,
        String descricao,
        String etapa,
        StatusProjeto status,
        Double investimento,
        Double retornoFinanceiro,
        Double aumentoProdutividade,
        LocalDate prazo,
        String estrategiaId,
        String ideiaId
) {
}