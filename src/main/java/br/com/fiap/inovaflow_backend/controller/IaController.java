package br.com.fiap.inovaflow_backend.controller;

import br.com.fiap.inovaflow_backend.dto.IaAnaliseIdeiaResponse;
import br.com.fiap.inovaflow_backend.dto.IaChatRequest;
import br.com.fiap.inovaflow_backend.dto.IaChatResponse;
import br.com.fiap.inovaflow_backend.service.IaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ia")
@RequiredArgsConstructor
public class IaController {

    private final IaService iaService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/chat")
    public ResponseEntity<IaChatResponse> chat(
            @Valid @RequestBody IaChatRequest request
    ) {

        return ResponseEntity.ok(
                iaService.chat(request)
        );
    }

    @PreAuthorize("hasRole('GESTOR')")
    @PostMapping("/ideias/{id}/analisar")
    public ResponseEntity<IaAnaliseIdeiaResponse> analisarIdeia(
            @PathVariable String id
    ) {

        return ResponseEntity.ok(
                iaService.analisarIdeia(id)
        );
    }
}