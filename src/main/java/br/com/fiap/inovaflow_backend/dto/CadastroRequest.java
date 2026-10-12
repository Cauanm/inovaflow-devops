package br.com.fiap.inovaflow_backend.dto;

import br.com.fiap.inovaflow_backend.model.PerfilUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CadastroRequest(

        @NotBlank(
                message = "O nome é obrigatório."
        )
        String nome,

        @NotBlank(
                message = "O e-mail é obrigatório."
        )
        @Email(
                message = "Informe um e-mail válido."
        )
        String email,

        @NotBlank(
                message = "A senha é obrigatória."
        )
        @Size(
                min = 6,
                max = 100,
                message = "A senha deve possuir entre 6 e 100 caracteres."
        )
        String senha,

        @NotNull(
                message = "O perfil é obrigatório."
        )
        PerfilUsuario perfil,

        @NotBlank(
                message = "A área é obrigatória."
        )
        String area

) {
}