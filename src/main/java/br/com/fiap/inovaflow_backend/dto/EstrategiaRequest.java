package br.com.fiap.inovaflow_backend.dto;

import br.com.fiap.inovaflow_backend.model.StatusEstrategia;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EstrategiaRequest(

        @NotBlank(
                message = "O título é obrigatório."
        )
        @Size(
                max = 150,
                message = "O título deve possuir no máximo 150 caracteres."
        )
        String titulo,

        @NotBlank(
                message = "A descrição é obrigatória."
        )
        @Size(
                max = 3000,
                message = "A descrição deve possuir no máximo 3000 caracteres."
        )
        String descricao,

        @NotBlank(
                message = "A categoria é obrigatória."
        )
        String categoria,

        @Size(
                max = 150,
                message = "A campanha deve possuir no máximo 150 caracteres."
        )
        String campanha,

        StatusEstrategia status

) {
}