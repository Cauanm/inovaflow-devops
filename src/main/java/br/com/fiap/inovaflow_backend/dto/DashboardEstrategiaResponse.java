package br.com.fiap.inovaflow_backend.dto;

public record DashboardEstrategiaResponse(
        String estrategiaId,
        long totalProjetos,
        Double investimentoTotal,
        Double retornoTotal,
        Double lucroTotal,
        Double roiMedio,
        Double produtividadeMedia
) {
}