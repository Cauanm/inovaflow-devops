package br.com.fiap.inovaflow_backend.dto;

import br.com.fiap.inovaflow_backend.model.StatusProjeto;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ProjetoRequest(

        @NotBlank(
                message = "O nome é obrigatório."
        )
        @Size(
                max = 150,
                message = "O nome deve possuir no máximo 150 caracteres."
        )
        String nome,

        @NotBlank(
                message = "A descrição é obrigatória."
        )
        @Size(
                max = 3000,
                message = "A descrição deve possuir no máximo 3000 caracteres."
        )
        String descricao,

        @NotBlank(
                message = "A etapa é obrigatória."
        )
        String etapa,

        StatusProjeto status,

        @PositiveOrZero(
                message = "O investimento não pode ser negativo."
        )
        Double investimento,

        @PositiveOrZero(
                message = "O retorno financeiro não pode ser negativo."
        )
        Double retornoFinanceiro,

        @PositiveOrZero(
                message = "O aumento de produtividade não pode ser negativo."
        )
        Double aumentoProdutividade,

        @FutureOrPresent(
                message = "O prazo não pode estar no passado."
        )
        LocalDate prazo,

        @NotBlank(
                message = "A estratégia é obrigatória."
        )
        String estrategiaId,

        String ideiaId

) {
}