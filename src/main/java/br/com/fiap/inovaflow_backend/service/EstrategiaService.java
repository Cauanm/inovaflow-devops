package br.com.fiap.inovaflow_backend.service;

import br.com.fiap.inovaflow_backend.dto.EstrategiaRequest;
import br.com.fiap.inovaflow_backend.dto.EstrategiaResponse;
import br.com.fiap.inovaflow_backend.model.Estrategia;
import br.com.fiap.inovaflow_backend.model.StatusEstrategia;
import br.com.fiap.inovaflow_backend.repository.EstrategiaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EstrategiaService {

    private final EstrategiaRepository estrategiaRepository;

    public List<EstrategiaResponse> listarTodas() {
        return estrategiaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public EstrategiaResponse buscarPorId(String id) {
        Estrategia estrategia = estrategiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estratégia não encontrada."));

        return toResponse(estrategia);
    }

    public EstrategiaResponse criar(EstrategiaRequest request) {
        Estrategia estrategia = new Estrategia();

        estrategia.setTitulo(request.titulo());
        estrategia.setDescricao(request.descricao());
        estrategia.setCategoria(request.categoria());
        estrategia.setCampanha(request.campanha());
        estrategia.setStatus(request.status() != null ? request.status() : StatusEstrategia.ATIVA);
        estrategia.setDataCriacao(LocalDateTime.now());

        Estrategia salva = estrategiaRepository.save(estrategia);

        return toResponse(salva);
    }

    public EstrategiaResponse atualizar(String id, EstrategiaRequest request) {
        Estrategia estrategia = estrategiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estratégia não encontrada."));

        estrategia.setTitulo(request.titulo());
        estrategia.setDescricao(request.descricao());
        estrategia.setCategoria(request.categoria());
        estrategia.setCampanha(request.campanha());
        estrategia.setStatus(request.status() != null ? request.status() : estrategia.getStatus());

        Estrategia atualizada = estrategiaRepository.save(estrategia);

        return toResponse(atualizada);
    }

    public void deletar(String id) {
        if (!estrategiaRepository.existsById(id)) {
            throw new RuntimeException("Estratégia não encontrada.");
        }

        estrategiaRepository.deleteById(id);
    }

    private EstrategiaResponse toResponse(Estrategia estrategia) {
        return new EstrategiaResponse(
                estrategia.getId(),
                estrategia.getTitulo(),
                estrategia.getDescricao(),
                estrategia.getCategoria(),
                estrategia.getCampanha(),
                estrategia.getStatus(),
                estrategia.getDataCriacao()
        );
    }
}