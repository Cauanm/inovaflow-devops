package br.com.fiap.inovaflow_backend.controller;

import br.com.fiap.inovaflow_backend.dto.IdeiaRequest;
import br.com.fiap.inovaflow_backend.dto.IdeiaResponse;
import br.com.fiap.inovaflow_backend.dto.PriorizarIdeiaRequest;
import br.com.fiap.inovaflow_backend.model.Usuario;
import br.com.fiap.inovaflow_backend.service.IdeiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ideias")
@RequiredArgsConstructor
public class IdeiaController {

    private final IdeiaService ideiaService;

    @PreAuthorize("hasRole('OPERADOR')")
    @PostMapping
    public ResponseEntity<IdeiaResponse> criar(
            @Valid @RequestBody IdeiaRequest request,
            Authentication authentication
    ) {

        Usuario operador =
                (Usuario) authentication.getPrincipal();

        IdeiaResponse response =
                ideiaService.criar(
                        request,
                        operador
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PreAuthorize("hasRole('OPERADOR')")
    @GetMapping("/minhas")
    public ResponseEntity<List<IdeiaResponse>> listarMinhas(
            Authentication authentication
    ) {

        Usuario operador =
                (Usuario) authentication.getPrincipal();

        return ResponseEntity.ok(
                ideiaService.listarMinhas(
                        operador
                )
        );
    }

    @PreAuthorize("hasAnyRole('GESTOR', 'LIDERANCA')")
    @GetMapping
    public ResponseEntity<List<IdeiaResponse>> listarTodas() {

        return ResponseEntity.ok(
                ideiaService.listarTodas()
        );
    }

    @PreAuthorize("hasAnyRole('OPERADOR', 'GESTOR', 'LIDERANCA')")
    @GetMapping("/{id}")
    public ResponseEntity<IdeiaResponse> buscarPorId(
            @PathVariable String id,
            Authentication authentication
    ) {

        Usuario usuario =
                (Usuario) authentication.getPrincipal();

        return ResponseEntity.ok(
                ideiaService.buscarPorId(
                        id,
                        usuario
                )
        );
    }

    @PreAuthorize("hasRole('OPERADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<IdeiaResponse> atualizar(
            @PathVariable String id,
            @Valid @RequestBody IdeiaRequest request,
            Authentication authentication
    ) {

        Usuario operador =
                (Usuario) authentication.getPrincipal();

        return ResponseEntity.ok(
                ideiaService.atualizar(
                        id,
                        request,
                        operador
                )
        );
    }

    @PreAuthorize("hasRole('OPERADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable String id,
            Authentication authentication
    ) {

        Usuario operador =
                (Usuario) authentication.getPrincipal();

        ideiaService.deletar(
                id,
                operador
        );

        return ResponseEntity
                .noContent()
                .build();
    }

    @PreAuthorize("hasRole('GESTOR')")
    @PatchMapping("/{id}/aprovar")
    public ResponseEntity<IdeiaResponse> aprovar(
            @PathVariable String id
    ) {

        return ResponseEntity.ok(
                ideiaService.aprovar(id)
        );
    }

    @PreAuthorize("hasRole('GESTOR')")
    @PatchMapping("/{id}/reprovar")
    public ResponseEntity<IdeiaResponse> reprovar(
            @PathVariable String id,
            @Valid @RequestBody PriorizarIdeiaRequest request
    ) {

        return ResponseEntity.ok(
                ideiaService.reprovar(
                        id,
                        request
                )
        );
    }

    @PreAuthorize("hasRole('GESTOR')")
    @PatchMapping("/{id}/priorizar")
    public ResponseEntity<IdeiaResponse> priorizar(
            @PathVariable String id,
            @Valid @RequestBody PriorizarIdeiaRequest request
    ) {

        return ResponseEntity.ok(
                ideiaService.priorizar(
                        id,
                        request
                )
        );
    }
}