package br.com.fiap.inovaflow_backend.controller;

import br.com.fiap.inovaflow_backend.dto.DashboardEstrategiaResponse;
import br.com.fiap.inovaflow_backend.dto.DashboardResumoResponse;
import br.com.fiap.inovaflow_backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @PreAuthorize("hasRole('LIDERANCA')")
    @GetMapping("/resumo")
    public ResponseEntity<DashboardResumoResponse> resumoGeral() {
        return ResponseEntity.ok(dashboardService.resumoGeral());
    }

    @PreAuthorize("hasRole('LIDERANCA')")
    @GetMapping("/estrategia/{estrategiaId}")
    public ResponseEntity<DashboardEstrategiaResponse> resumoPorEstrategia(
            @PathVariable String estrategiaId
    ) {
        return ResponseEntity.ok(dashboardService.resumoPorEstrategia(estrategiaId));
    }
}