package br.com.fiap.inovaflow_backend.service;

import br.com.fiap.inovaflow_backend.dto.ProjetoRequest;
import br.com.fiap.inovaflow_backend.dto.ProjetoResponse;
import br.com.fiap.inovaflow_backend.exception.RecursoNaoEncontradoException;
import br.com.fiap.inovaflow_backend.exception.RegraNegocioException;
import br.com.fiap.inovaflow_backend.model.Ideia;
import br.com.fiap.inovaflow_backend.model.Projeto;
import br.com.fiap.inovaflow_backend.model.StatusIdeia;
import br.com.fiap.inovaflow_backend.model.StatusProjeto;
import br.com.fiap.inovaflow_backend.repository.EstrategiaRepository;
import br.com.fiap.inovaflow_backend.repository.IdeiaRepository;
import br.com.fiap.inovaflow_backend.repository.ProjetoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final EstrategiaRepository estrategiaRepository;
    private final IdeiaRepository ideiaRepository;

    public ProjetoResponse criar(
            ProjetoRequest request
    ) {

        validarEstrategia(
                request.estrategiaId()
        );

        validarIdeiaSeInformada(
                request.ideiaId()
        );

        Projeto projeto = new Projeto();

        projeto.setNome(request.nome());
        projeto.setDescricao(request.descricao());
        projeto.setEtapa(request.etapa());

        projeto.setStatus(
                request.status() != null
                        ? request.status()
                        : StatusProjeto.PLANEJADO
        );

        projeto.setInvestimento(
                valorOuZero(request.investimento())
        );

        projeto.setRetornoFinanceiro(
                valorOuZero(request.retornoFinanceiro())
        );

        projeto.setAumentoProdutividade(
                valorOuZero(request.aumentoProdutividade())
        );

        projeto.setPrazo(request.prazo());
        projeto.setEstrategiaId(request.estrategiaId());
        projeto.setIdeiaId(request.ideiaId());

        calcularResultados(projeto);

        projeto.setDataCriacao(LocalDateTime.now());
        projeto.setDataAtualizacao(LocalDateTime.now());

        Projeto salvo =
                projetoRepository.save(projeto);

        return toResponse(salvo);
    }

    public List<ProjetoResponse> listarTodos() {

        return projetoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ProjetoResponse buscarPorId(String id) {

        Projeto projeto = buscarProjeto(id);

        return toResponse(projeto);
    }

    public ProjetoResponse atualizar(
            String id,
            ProjetoRequest request
    ) {

        Projeto projeto = buscarProjeto(id);

        validarEstrategia(
                request.estrategiaId()
        );

        validarIdeiaSeInformada(
                request.ideiaId()
        );

        projeto.setNome(request.nome());
        projeto.setDescricao(request.descricao());
        projeto.setEtapa(request.etapa());

        projeto.setStatus(
                request.status() != null
                        ? request.status()
                        : projeto.getStatus()
        );

        projeto.setInvestimento(
                valorOuZero(request.investimento())
        );

        projeto.setRetornoFinanceiro(
                valorOuZero(request.retornoFinanceiro())
        );

        projeto.setAumentoProdutividade(
                valorOuZero(request.aumentoProdutividade())
        );

        projeto.setPrazo(request.prazo());
        projeto.setEstrategiaId(request.estrategiaId());
        projeto.setIdeiaId(request.ideiaId());

        calcularResultados(projeto);

        projeto.setDataAtualizacao(LocalDateTime.now());

        Projeto atualizado =
                projetoRepository.save(projeto);

        return toResponse(atualizado);
    }

    public void deletar(String id) {

        if (!projetoRepository.existsById(id)) {

            throw new RecursoNaoEncontradoException(
                    "Projeto não encontrado."
            );
        }

        projetoRepository.deleteById(id);
    }

    public List<ProjetoResponse> listarPorEstrategia(
            String estrategiaId
    ) {

        validarEstrategia(estrategiaId);

        return projetoRepository
                .findByEstrategiaId(estrategiaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private Projeto buscarProjeto(String id) {

        return projetoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Projeto não encontrado."
                        )
                );
    }

    private void validarEstrategia(
            String estrategiaId
    ) {

        if (estrategiaId == null ||
                estrategiaId.isBlank()) {

            throw new RegraNegocioException(
                    "O projeto precisa estar vinculado a uma estratégia."
            );
        }

        if (!estrategiaRepository.existsById(
                estrategiaId
        )) {

            throw new RecursoNaoEncontradoException(
                    "Estratégia não encontrada."
            );
        }
    }

    private void validarIdeiaSeInformada(
            String ideiaId
    ) {

        if (ideiaId == null ||
                ideiaId.isBlank()) {
            return;
        }

        Ideia ideia = ideiaRepository.findById(
                        ideiaId
                )
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Ideia não encontrada."
                        )
                );

        if (ideia.getStatus() !=
                StatusIdeia.APROVADA) {

            throw new RegraNegocioException(
                    "Só é possível criar projeto a partir de uma ideia aprovada."
            );
        }
    }

    private void calcularResultados(
            Projeto projeto
    ) {

        double investimento =
                valorOuZero(projeto.getInvestimento());

        double retorno =
                valorOuZero(
                        projeto.getRetornoFinanceiro()
                );

        double lucro =
                retorno - investimento;

        projeto.setLucroObtido(lucro);

        if (investimento > 0) {

            double roi =
                    (lucro / investimento) * 100;

            projeto.setRoi(roi);

        } else {

            projeto.setRoi(0.0);
        }
    }

    private Double valorOuZero(Double valor) {

        return valor != null
                ? valor
                : 0.0;
    }

    private ProjetoResponse toResponse(
            Projeto projeto
    ) {

        return new ProjetoResponse(
                projeto.getId(),
                projeto.getNome(),
                projeto.getDescricao(),
                projeto.getEtapa(),
                projeto.getStatus(),
                projeto.getInvestimento(),
                projeto.getRetornoFinanceiro(),
                projeto.getLucroObtido(),
                projeto.getRoi(),
                projeto.getAumentoProdutividade(),
                projeto.getPrazo(),
                projeto.getEstrategiaId(),
                projeto.getIdeiaId(),
                projeto.getDataCriacao(),
                projeto.getDataAtualizacao()
        );
    }
}