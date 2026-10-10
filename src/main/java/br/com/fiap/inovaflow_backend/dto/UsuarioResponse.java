package br.com.fiap.inovaflow_backend.dto;

import br.com.fiap.inovaflow_backend.model.PerfilUsuario;

public record UsuarioResponse(
        String id,
        String nome,
        String email,
        PerfilUsuario perfil,
        String area,
        Integer pontosInovacao
) {
}