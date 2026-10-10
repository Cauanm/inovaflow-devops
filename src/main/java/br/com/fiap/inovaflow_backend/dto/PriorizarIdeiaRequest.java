package br.com.fiap.inovaflow_backend.dto;

import br.com.fiap.inovaflow_backend.model.PrioridadeIdeia;

public record PriorizarIdeiaRequest(
        PrioridadeIdeia prioridade,
        String observacaoGestor
) {
}