package br.com.fiap.inovaflow_backend.dto;

import br.com.fiap.inovaflow_backend.model.PrioridadeIdeia;
import jakarta.validation.constraints.Size;

public record PriorizarIdeiaRequest(

        PrioridadeIdeia prioridade,

        @Size(
                max = 500,
                message = "A observação deve possuir no máximo 500 caracteres."
        )
        String observacaoGestor

) {
}