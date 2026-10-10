package br.com.fiap.inovaflow_backend.dto;

import br.com.fiap.inovaflow_backend.model.PerfilUsuario;

public record CadastroRequest(
        String nome,
        String email,
        String senha,
        PerfilUsuario perfil,
        String area
) {
}