package br.com.fiap.inovaflow_backend.controller;

import br.com.fiap.inovaflow_backend.model.Usuario;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TesteController {

    @GetMapping("/teste/logado")
    public String usuarioLogado(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return "Usuário logado: " + usuario.getNome() + " - Perfil: " + usuario.getPerfil();
    }

    @PreAuthorize("hasRole('OPERADOR')")
    @GetMapping("/teste/operador")
    public String somenteOperador() {
        return "Acesso liberado para OPERADOR";
    }

    @PreAuthorize("hasRole('GESTOR')")
    @GetMapping("/teste/gestor")
    public String somenteGestor() {
        return "Acesso liberado para GESTOR";
    }

    @PreAuthorize("hasRole('LIDERANCA')")
    @GetMapping("/teste/lideranca")
    public String somenteLideranca() {
        return "Acesso liberado para LIDERANCA";
    }
}