package br.com.fiap.inovaflow_backend.controller;

import br.com.fiap.inovaflow_backend.dto.CadastroRequest;
import br.com.fiap.inovaflow_backend.dto.LoginRequest;
import br.com.fiap.inovaflow_backend.dto.LoginResponse;
import br.com.fiap.inovaflow_backend.dto.UsuarioResponse;
import br.com.fiap.inovaflow_backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioResponse> cadastrar(@RequestBody CadastroRequest request) {
        UsuarioResponse response = usuarioService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = usuarioService.login(request);
        return ResponseEntity.ok(response);
    }
}