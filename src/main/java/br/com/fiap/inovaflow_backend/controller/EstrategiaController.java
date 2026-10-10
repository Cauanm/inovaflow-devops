package br.com.fiap.inovaflow_backend.controller;

import br.com.fiap.inovaflow_backend.dto.EstrategiaRequest;
import br.com.fiap.inovaflow_backend.dto.EstrategiaResponse;
import br.com.fiap.inovaflow_backend.service.EstrategiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estrategias")
@RequiredArgsConstructor
public class EstrategiaController {

    private final EstrategiaService estrategiaService;

    @PreAuthorize("hasAnyRole('OPERADOR', 'GESTOR', 'LIDERANCA')")
    @GetMapping
    public ResponseEntity<List<EstrategiaResponse>> listarTodas() {
        return ResponseEntity.ok(estrategiaService.listarTodas());
    }

    @PreAuthorize("hasAnyRole('OPERADOR', 'GESTOR', 'LIDERANCA')")
    @GetMapping("/{id}")
    public ResponseEntity<EstrategiaResponse> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(estrategiaService.buscarPorId(id));
    }

    @PreAuthorize("hasRole('LIDERANCA')")
    @PostMapping
    public ResponseEntity<EstrategiaResponse> criar(@RequestBody EstrategiaRequest request) {
        EstrategiaResponse response = estrategiaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('LIDERANCA')")
    @PutMapping("/{id}")
    public ResponseEntity<EstrategiaResponse> atualizar(
            @PathVariable String id,
            @RequestBody EstrategiaRequest request
    ) {
        return ResponseEntity.ok(estrategiaService.atualizar(id, request));
    }

    @PreAuthorize("hasRole('LIDERANCA')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        estrategiaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}