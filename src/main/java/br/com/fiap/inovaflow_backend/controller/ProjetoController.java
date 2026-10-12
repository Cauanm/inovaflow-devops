package br.com.fiap.inovaflow_backend.controller;

import br.com.fiap.inovaflow_backend.dto.ProjetoRequest;
import br.com.fiap.inovaflow_backend.dto.ProjetoResponse;
import br.com.fiap.inovaflow_backend.service.ProjetoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projetos")
@RequiredArgsConstructor
public class ProjetoController {

    private final ProjetoService projetoService;

    @PreAuthorize("hasAnyRole('GESTOR', 'LIDERANCA')")
    @GetMapping
    public ResponseEntity<List<ProjetoResponse>> listarTodos() {

        return ResponseEntity.ok(
                projetoService.listarTodos()
        );
    }

    @PreAuthorize("hasAnyRole('GESTOR', 'LIDERANCA')")
    @GetMapping("/{id}")
    public ResponseEntity<ProjetoResponse> buscarPorId(
            @PathVariable String id
    ) {

        return ResponseEntity.ok(
                projetoService.buscarPorId(id)
        );
    }

    @PreAuthorize("hasAnyRole('GESTOR', 'LIDERANCA')")
    @GetMapping("/estrategia/{estrategiaId}")
    public ResponseEntity<List<ProjetoResponse>> listarPorEstrategia(
            @PathVariable String estrategiaId
    ) {

        return ResponseEntity.ok(
                projetoService.listarPorEstrategia(
                        estrategiaId
                )
        );
    }

    @PreAuthorize("hasRole('GESTOR')")
    @PostMapping
    public ResponseEntity<ProjetoResponse> criar(
            @Valid @RequestBody ProjetoRequest request
    ) {

        ProjetoResponse response =
                projetoService.criar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PreAuthorize("hasRole('GESTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ProjetoResponse> atualizar(
            @PathVariable String id,
            @Valid @RequestBody ProjetoRequest request
    ) {

        return ResponseEntity.ok(
                projetoService.atualizar(
                        id,
                        request
                )
        );
    }

    @PreAuthorize("hasRole('GESTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable String id
    ) {

        projetoService.deletar(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}