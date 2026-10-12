package br.com.fiap.inovaflow_backend.service;

import br.com.fiap.inovaflow_backend.dto.CadastroRequest;
import br.com.fiap.inovaflow_backend.dto.LoginRequest;
import br.com.fiap.inovaflow_backend.dto.LoginResponse;
import br.com.fiap.inovaflow_backend.dto.UsuarioResponse;
import br.com.fiap.inovaflow_backend.exception.ConflitoException;
import br.com.fiap.inovaflow_backend.exception.CredenciaisInvalidasException;
import br.com.fiap.inovaflow_backend.model.Usuario;
import br.com.fiap.inovaflow_backend.repository.UsuarioRepository;
import br.com.fiap.inovaflow_backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UsuarioResponse cadastrar(CadastroRequest request) {

        if (usuarioRepository.existsByEmail(request.email())) {
            throw new ConflitoException("E-mail já cadastrado.");
        }

        Usuario usuario = new Usuario();

        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setPerfil(request.perfil());
        usuario.setArea(request.area());
        usuario.setPontosInovacao(0);

        Usuario salvo = usuarioRepository.save(usuario);

        return toUsuarioResponse(salvo);
    }

    public LoginResponse login(LoginRequest request) {

        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new CredenciaisInvalidasException(
                                "E-mail ou senha inválidos."
                        )
                );

        if (!passwordEncoder.matches(
                request.senha(),
                usuario.getSenha()
        )) {
            throw new CredenciaisInvalidasException(
                    "E-mail ou senha inválidos."
            );
        }

        String token = jwtService.gerarToken(usuario);

        return new LoginResponse(
                token,
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil(),
                usuario.getArea(),
                usuario.getPontosInovacao()
        );
    }

    private UsuarioResponse toUsuarioResponse(Usuario usuario) {

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil(),
                usuario.getArea(),
                usuario.getPontosInovacao()
        );
    }
}