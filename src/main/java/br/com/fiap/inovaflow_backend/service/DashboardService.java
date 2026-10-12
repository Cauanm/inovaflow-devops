package br.com.fiap.inovaflow_backend.service;

import br.com.fiap.inovaflow_backend.dto.DashboardEstrategiaResponse;
import br.com.fiap.inovaflow_backend.dto.DashboardResumoResponse;
import br.com.fiap.inovaflow_backend.exception.RecursoNaoEncontradoException;
import br.com.fiap.inovaflow_backend.model.Projeto;
import br.com.fiap.inovaflow_backend.repository.EstrategiaRepository;
import br.com.fiap.inovaflow_backend.repository.ProjetoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProjetoRepository projetoRepository;
    private final EstrategiaRepository estrategiaRepository;

    public DashboardResumoResponse resumoGeral() {

        List<Projeto> projetos = projetoRepository.findAll();

        double investimentoTotal = projetos.stream()
                .mapToDouble(p ->
                        valorOuZero(p.getInvestimento()))
                .sum();

        double retornoTotal = projetos.stream()
                .mapToDouble(p ->
                        valorOuZero(p.getRetornoFinanceiro()))
                .sum();

        double lucroTotal = projetos.stream()
                .mapToDouble(p ->
                        valorOuZero(p.getLucroObtido()))
                .sum();

        double roiMedio = projetos.stream()
                .mapToDouble(p ->
                        valorOuZero(p.getRoi()))
                .average()
                .orElse(0.0);

        double produtividadeMedia = projetos.stream()
                .mapToDouble(p ->
                        valorOuZero(p.getAumentoProdutividade()))
                .average()
                .orElse(0.0);

        Map<String, Long> projetosPorStatus =
                projetos.stream()
                        .collect(Collectors.groupingBy(
                                p -> p.getStatus().name(),
                                Collectors.counting()
                        ));

        return new DashboardResumoResponse(
                projetos.size(),
                investimentoTotal,
                retornoTotal,
                lucroTotal,
                roiMedio,
                produtividadeMedia,
                projetosPorStatus
        );
    }

    public DashboardEstrategiaResponse resumoPorEstrategia(
            String estrategiaId
    ) {

        if (!estrategiaRepository.existsById(estrategiaId)) {
            throw new RecursoNaoEncontradoException(
                    "Estratégia não encontrada."
            );
        }

        List<Projeto> projetos =
                projetoRepository.findByEstrategiaId(estrategiaId);

        double investimentoTotal = projetos.stream()
                .mapToDouble(p ->
                        valorOuZero(p.getInvestimento()))
                .sum();

        double retornoTotal = projetos.stream()
                .mapToDouble(p ->
                        valorOuZero(p.getRetornoFinanceiro()))
                .sum();

        double lucroTotal = projetos.stream()
                .mapToDouble(p ->
                        valorOuZero(p.getLucroObtido()))
                .sum();

        double roiMedio = projetos.stream()
                .mapToDouble(p ->
                        valorOuZero(p.getRoi()))
                .average()
                .orElse(0.0);

        double produtividadeMedia = projetos.stream()
                .mapToDouble(p ->
                        valorOuZero(p.getAumentoProdutividade()))
                .average()
                .orElse(0.0);

        return new DashboardEstrategiaResponse(
                estrategiaId,
                projetos.size(),
                investimentoTotal,
                retornoTotal,
                lucroTotal,
                roiMedio,
                produtividadeMedia
        );
    }

    private Double valorOuZero(Double valor) {
        return valor != null ? valor : 0.0;
    }
}