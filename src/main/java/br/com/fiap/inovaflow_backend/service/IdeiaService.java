package br.com.fiap.inovaflow_backend.service;

import br.com.fiap.inovaflow_backend.dto.IdeiaRequest;
import br.com.fiap.inovaflow_backend.dto.IdeiaResponse;
import br.com.fiap.inovaflow_backend.dto.PriorizarIdeiaRequest;
import br.com.fiap.inovaflow_backend.model.*;
import br.com.fiap.inovaflow_backend.repository.EstrategiaRepository;
import br.com.fiap.inovaflow_backend.repository.IdeiaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IdeiaService {

    private final IdeiaRepository ideiaRepository;
    private final EstrategiaRepository estrategiaRepository;

    public IdeiaResponse criar(IdeiaRequest request, Usuario operador) {
        validarEstrategia(request.estrategiaId());

        Ideia ideia = new Ideia();
        ideia.setTitulo(request.titulo());
        ideia.setDescricao(request.descricao());
        ideia.setCategoria(request.categoria());
        ideia.setEstrategiaId(request.estrategiaId());

        ideia.setStatus(StatusIdeia.AGUARDANDO);
        ideia.setPrioridade(PrioridadeIdeia.MEDIA);

        ideia.setOperadorId(operador.getId());
        ideia.setOperadorNome(operador.getNome());

        ideia.setDataCriacao(LocalDateTime.now());
        ideia.setDataAtualizacao(LocalDateTime.now());

        Ideia salva = ideiaRepository.save(ideia);

        return toResponse(salva);
    }

    public List<IdeiaResponse> listarTodas() {
        return ideiaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<IdeiaResponse> listarMinhas(Usuario operador) {
        return ideiaRepository.findByOperadorId(operador.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public IdeiaResponse buscarPorId(String id, Usuario usuario) {
        Ideia ideia = buscarIdeia(id);

        if (usuario.getPerfil() == PerfilUsuario.OPERADOR &&
                !ideia.getOperadorId().equals(usuario.getId())) {
            throw new RuntimeException("Você não tem permissão para consultar esta ideia.");
        }

        return toResponse(ideia);
    }

    public IdeiaResponse atualizar(String id, IdeiaRequest request, Usuario operador) {
        Ideia ideia = buscarIdeia(id);

        if (!ideia.getOperadorId().equals(operador.getId())) {
            throw new RuntimeException("Você só pode editar suas próprias ideias.");
        }

        if (ideia.getStatus() != StatusIdeia.AGUARDANDO) {
            throw new RuntimeException("Só é possível editar ideias que ainda estão aguardando análise.");
        }

        validarEstrategia(request.estrategiaId());

        ideia.setTitulo(request.titulo());
        ideia.setDescricao(request.descricao());
        ideia.setCategoria(request.categoria());
        ideia.setEstrategiaId(request.estrategiaId());
        ideia.setDataAtualizacao(LocalDateTime.now());

        Ideia atualizada = ideiaRepository.save(ideia);

        return toResponse(atualizada);
    }

    public void deletar(String id, Usuario operador) {
        Ideia ideia = buscarIdeia(id);

        if (!ideia.getOperadorId().equals(operador.getId())) {
            throw new RuntimeException("Você só pode excluir suas próprias ideias.");
        }

        if (ideia.getStatus() != StatusIdeia.AGUARDANDO) {
            throw new RuntimeException("Só é possível excluir ideias que ainda estão aguardando análise.");
        }

        ideiaRepository.deleteById(id);
    }

    public IdeiaResponse aprovar(String id) {
        Ideia ideia = buscarIdeia(id);

        ideia.setStatus(StatusIdeia.APROVADA);
        ideia.setDataAtualizacao(LocalDateTime.now());

        Ideia salva = ideiaRepository.save(ideia);

        return toResponse(salva);
    }

    public IdeiaResponse reprovar(String id, PriorizarIdeiaRequest request) {
        Ideia ideia = buscarIdeia(id);

        ideia.setStatus(StatusIdeia.REPROVADA);
        ideia.setObservacaoGestor(request.observacaoGestor());
        ideia.setDataAtualizacao(LocalDateTime.now());

        Ideia salva = ideiaRepository.save(ideia);

        return toResponse(salva);
    }

    public IdeiaResponse priorizar(String id, PriorizarIdeiaRequest request) {
        Ideia ideia = buscarIdeia(id);

        ideia.setPrioridade(request.prioridade());
        ideia.setObservacaoGestor(request.observacaoGestor());
        ideia.setDataAtualizacao(LocalDateTime.now());

        Ideia salva = ideiaRepository.save(ideia);

        return toResponse(salva);
    }

    private Ideia buscarIdeia(String id) {
        return ideiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ideia não encontrada."));
    }

    private void validarEstrategia(String estrategiaId) {
        if (estrategiaId == null || estrategiaId.isBlank()) {
            throw new RuntimeException("A ideia precisa estar vinculada a uma estratégia.");
        }

        if (!estrategiaRepository.existsById(estrategiaId)) {
            throw new RuntimeException("Estratégia não encontrada.");
        }
    }

    private IdeiaResponse toResponse(Ideia ideia) {
        return new IdeiaResponse(
                ideia.getId(),
                ideia.getTitulo(),
                ideia.getDescricao(),
                ideia.getCategoria(),
                ideia.getStatus(),
                ideia.getPrioridade(),
                ideia.getOperadorId(),
                ideia.getOperadorNome(),
                ideia.getEstrategiaId(),
                ideia.getObservacaoGestor(),
                ideia.getDataCriacao(),
                ideia.getDataAtualizacao(),
                ideia.getPontuacaoIa(),
                ideia.getPrioridadeSugeridaIa(),
                ideia.getJustificativaIa()
        );
    }
}