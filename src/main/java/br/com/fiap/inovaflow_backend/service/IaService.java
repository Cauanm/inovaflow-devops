package br.com.fiap.inovaflow_backend.service;

import br.com.fiap.inovaflow_backend.dto.IaAnaliseIdeiaResponse;
import br.com.fiap.inovaflow_backend.dto.IaChatRequest;
import br.com.fiap.inovaflow_backend.dto.IaChatResponse;
import br.com.fiap.inovaflow_backend.exception.IntegracaoExternaException;
import br.com.fiap.inovaflow_backend.exception.RecursoNaoEncontradoException;
import br.com.fiap.inovaflow_backend.model.AnaliseIa;
import br.com.fiap.inovaflow_backend.model.Ideia;
import br.com.fiap.inovaflow_backend.model.PrioridadeIdeia;
import br.com.fiap.inovaflow_backend.repository.AnaliseIaRepository;
import br.com.fiap.inovaflow_backend.repository.IdeiaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IaService {

    private final IdeiaRepository ideiaRepository;

    private final AnaliseIaRepository analiseIaRepository;

    /*
     * Jackson 3.
     * O JsonMapper é fornecido automaticamente pelo Spring Boot 4.
     */
    private final JsonMapper jsonMapper;

    /*
     * Cliente HTTP utilizado para comunicação com a API Gemini.
     */
    private final HttpClient httpClient =
            HttpClient.newBuilder()
                    .connectTimeout(
                            Duration.ofSeconds(10)
                    )
                    .build();


    /*
     * Variáveis carregadas do application.properties.
     */
    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.model}")
    private String model;


    /*
     * Chat geral com a IA.
     */
    public IaChatResponse chat(
            IaChatRequest request
    ) {

        String prompt = """
                Você é um assistente de inovação corporativa
                da plataforma InovaFlow.

                Ajude operadores, gestores e líderes a:

                - analisar ideias;
                - estruturar projetos;
                - melhorar processos;
                - reduzir custos;
                - aumentar produtividade;
                - avaliar iniciativas de sustentabilidade.

                Responda em português do Brasil,
                de forma objetiva, clara e profissional.

                Mensagem do usuário:

                %s
                """.formatted(
                request.mensagem()
        );

        String resposta =
                chamarGemini(prompt);

        return new IaChatResponse(
                resposta
        );
    }


    /*
     * Analisa uma ideia existente utilizando IA.
     */
    public IaAnaliseIdeiaResponse analisarIdeia(
            String ideiaId
    ) {

        Ideia ideia =
                ideiaRepository.findById(
                                ideiaId
                        )
                        .orElseThrow(() ->
                                new RecursoNaoEncontradoException(
                                        "Ideia não encontrada."
                                )
                        );


        String prompt = """
                Analise a seguinte ideia de inovação
                para auxiliar um gestor na priorização.

                Título:
                %s

                Descrição:
                %s

                Categoria:
                %s

                Status atual:
                %s

                Prioridade atual:
                %s

                Retorne APENAS um JSON válido.

                Não utilize Markdown.
                Não escreva explicações fora do JSON.

                O JSON deve possuir exatamente esta estrutura:

                {
                  "pontuacao": 0,
                  "prioridadeSugerida": "BAIXA",
                  "justificativa": "texto curto"
                }

                Regras:

                - pontuacao deve ser um número entre 0 e 100;
                - prioridadeSugerida deve ser BAIXA, MEDIA ou ALTA;
                - justificativa deve explicar brevemente:
                  potencial de impacto,
                  viabilidade
                  e aderência estratégica.
                """.formatted(
                ideia.getTitulo(),
                ideia.getDescricao(),
                ideia.getCategoria(),
                ideia.getStatus(),
                ideia.getPrioridade()
        );


        String respostaIa =
                chamarGemini(prompt);


        final Integer pontuacao;

        final PrioridadeIdeia prioridadeSugerida;

        final String justificativa;


        try {

            /*
             * Às vezes a IA pode retornar algum texto
             * antes/depois do JSON.
             *
             * Por isso extraímos somente o bloco JSON.
             */
            String jsonLimpo =
                    extrairJson(
                            respostaIa
                    );


            JsonNode node =
                    jsonMapper.readTree(
                            jsonLimpo
                    );


            /*
             * Verifica se todos os campos obrigatórios
             * foram retornados pela IA.
             */
            if (!node.has("pontuacao")
                    || !node.has("prioridadeSugerida")
                    || !node.has("justificativa")) {

                throw new IntegracaoExternaException(
                        "A IA retornou uma resposta incompleta."
                );
            }


            pontuacao =
                    node.get(
                                    "pontuacao"
                            )
                            .asInt();


            /*
             * Validação da pontuação.
             */
            if (pontuacao < 0
                    || pontuacao > 100) {

                throw new IntegracaoExternaException(
                        "A IA retornou uma pontuação inválida."
                );
            }


            /*
             * Converte a prioridade retornada pela IA
             * para o enum utilizado pelo sistema.
             */
            prioridadeSugerida =
                    PrioridadeIdeia.valueOf(
                            node.get(
                                            "prioridadeSugerida"
                                    )
                                    .asText()
                                    .toUpperCase()
                    );


            justificativa =
                    node.get(
                                    "justificativa"
                            )
                            .asText();


            if (justificativa == null
                    || justificativa.isBlank()) {

                throw new IntegracaoExternaException(
                        "A IA retornou uma justificativa inválida."
                );
            }


        } catch (IntegracaoExternaException e) {

            throw e;


        } catch (IllegalArgumentException e) {

            /*
             * Exemplo:
             * Gemini retorna "URGENTE"
             * em vez de BAIXA, MEDIA ou ALTA.
             */
            throw new IntegracaoExternaException(
                    "A IA retornou uma prioridade inválida.",
                    e
            );


        } catch (Exception e) {

            throw new IntegracaoExternaException(
                    "A IA retornou uma resposta em formato inválido.",
                    e
            );
        }


        /*
         * Salva a análise diretamente na ideia.
         */
        ideia.setPontuacaoIa(
                pontuacao
        );

        ideia.setPrioridadeSugeridaIa(
                prioridadeSugerida
        );

        ideia.setJustificativaIa(
                justificativa
        );


        ideiaRepository.save(
                ideia
        );


        /*
         * Também salva o histórico da análise
         * na collection de análises de IA.
         */
        AnaliseIa analise =
                new AnaliseIa();

        analise.setIdeiaId(
                ideia.getId()
        );

        analise.setPontuacao(
                pontuacao
        );

        analise.setPrioridadeSugerida(
                prioridadeSugerida
        );

        analise.setJustificativa(
                justificativa
        );

        analise.setDataAnalise(
                LocalDateTime.now()
        );


        AnaliseIa salva =
                analiseIaRepository.save(
                        analise
                );


        return new IaAnaliseIdeiaResponse(
                ideia.getId(),
                salva.getPontuacao(),
                salva.getPrioridadeSugerida(),
                salva.getJustificativa(),
                salva.getDataAnalise()
        );
    }


    /*
     * Responsável pela comunicação HTTP com Gemini.
     */
    private String chamarGemini(
            String prompt
    ) {

        /*
         * Evita realizar chamada sem chave configurada.
         */
        if (apiKey == null
                || apiKey.isBlank()) {

            throw new IntegracaoExternaException(
                    "O serviço de IA não está configurado."
            );
        }


        try {

            String url =
                    "https://generativelanguage.googleapis.com/v1beta/models/"
                            + model
                            + ":generateContent?key="
                            + apiKey;


            /*
             * Corpo esperado pela API Gemini.
             */
            Map<String, Object> body =
                    Map.of(
                            "contents",
                            List.of(
                                    Map.of(
                                            "parts",
                                            List.of(
                                                    Map.of(
                                                            "text",
                                                            prompt
                                                    )
                                            )
                                    )
                            )
                    );


            String jsonBody =
                    jsonMapper.writeValueAsString(
                            body
                    );


            HttpRequest request =
                    HttpRequest
                            .newBuilder()

                            .uri(
                                    URI.create(url)
                            )

                            .timeout(
                                    Duration.ofSeconds(30)
                            )

                            .header(
                                    "Content-Type",
                                    "application/json"
                            )

                            .POST(
                                    HttpRequest
                                            .BodyPublishers
                                            .ofString(
                                                    jsonBody
                                            )
                            )

                            .build();


            HttpResponse<String> response =
                    httpClient.send(
                            request,
                            HttpResponse
                                    .BodyHandlers
                                    .ofString()
                    );


            /*
             * Qualquer resposta fora da faixa 2xx
             * será tratada como falha da integração.
             */
            if (response.statusCode() < 200
                    || response.statusCode() >= 300) {

                throw new IntegracaoExternaException(
                        "O serviço de IA retornou erro. Status HTTP: "
                                + response.statusCode()
                );
            }


            JsonNode root =
                    jsonMapper.readTree(
                            response.body()
                    );


            /*
             * Estrutura padrão da resposta Gemini:
             *
             * candidates[0]
             *   .content
             *   .parts[0]
             *   .text
             */
            JsonNode textoNode =
                    root.path(
                                    "candidates"
                            )
                            .path(0)
                            .path(
                                    "content"
                            )
                            .path(
                                    "parts"
                            )
                            .path(0)
                            .path(
                                    "text"
                            );


            String texto =
                    textoNode.asText();


            if (texto == null
                    || texto.isBlank()) {

                throw new IntegracaoExternaException(
                        "O serviço de IA retornou uma resposta vazia."
                );
            }


            return texto;


        } catch (IntegracaoExternaException e) {

            throw e;


        } catch (InterruptedException e) {

            /*
             * Restaura o estado de interrupção da thread.
             */
            Thread.currentThread()
                    .interrupt();

            throw new IntegracaoExternaException(
                    "A comunicação com o serviço de IA foi interrompida.",
                    e
            );


        } catch (Exception e) {

            throw new IntegracaoExternaException(
                    "Não foi possível comunicar com o serviço de IA.",
                    e
            );
        }
    }


    /*
     * Extrai somente o bloco JSON de uma resposta.
     */
    private String extrairJson(
            String texto
    ) {

        if (texto == null
                || texto.isBlank()) {

            throw new IntegracaoExternaException(
                    "A resposta da IA está vazia."
            );
        }


        int inicio =
                texto.indexOf("{");

        int fim =
                texto.lastIndexOf("}");


        if (inicio == -1
                || fim == -1
                || fim <= inicio) {

            throw new IntegracaoExternaException(
                    "A resposta da IA não contém um JSON válido."
            );
        }


        return texto.substring(
                inicio,
                fim + 1
        );
    }
}