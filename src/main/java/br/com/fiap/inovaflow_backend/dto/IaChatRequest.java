package br.com.fiap.inovaflow_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IaChatRequest(

        @NotBlank(
                message = "A mensagem é obrigatória."
        )
        @Size(
                max = 4000,
                message = "A mensagem deve possuir no máximo 4000 caracteres."
        )
        String mensagem

) {
}