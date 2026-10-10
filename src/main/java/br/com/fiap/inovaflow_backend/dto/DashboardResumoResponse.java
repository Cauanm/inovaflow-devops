package br.com.fiap.inovaflow_backend.dto;

import java.util.Map;

public record DashboardResumoResponse(
        long totalProjetos,
        Double investimentoTotal,
        Double retornoTotal,
        Double lucroTotal,
        Double roiMedio,
        Double produtividadeMedia,
        Map<String, Long> projetosPorStatus
) {
}