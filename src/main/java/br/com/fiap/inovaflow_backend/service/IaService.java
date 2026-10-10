package br.com.fiap.inovaflow_backend.service;

import br.com.fiap.inovaflow_backend.dto.IaAnaliseIdeiaResponse;
import br.com.fiap.inovaflow_backend.dto.IaChatRequest;
import br.com.fiap.inovaflow_backend.dto.IaChatResponse;
import br.com.fiap.inovaflow_backend.model.AnaliseIa;
import br.com.fiap.inovaflow_backend.model.Ideia;
import br.com.fiap.inovaflow_backend.model.PrioridadeIdeia;
import br.com.fiap.inovaflow_backend.repository.AnaliseIaRepository;
import br.com.fiap.inovaflow_backend.repository.IdeiaRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IaService {

    private final IdeiaRepository ideiaRepository;
    private final AnaliseIaRepository analiseIaRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.model}")
    private String model;

    public IaChatResponse chat(IaChatRequest request) {
        String prompt = """
                Você é um assistente de inovação corporativa do projeto InovaFlow.
                Ajude gestores e líderes a analisar ideias, estruturar projetos,
                melhorar processos, reduzir custos e aumentar produtividade.

                Responda em português do Brasil, de forma objetiva e profissional.

                Mensagem do usuário:
                %s
                """.formatted(request.mensagem());

        String resposta = chamarGemini(prompt);

        return new IaChatResponse(resposta);
    }

    public IaAnaliseIdeiaResponse analisarIdeia(String ideiaId) {
        Ideia ideia = ideiaRepository.findById(ideiaId)
                .orElseThrow(() -> new RuntimeException("Ideia não encontrada."));

        String prompt = """
                Analise a ideia de inovação abaixo para auxiliar um gestor na priorização.

                Título: %s
                Descrição: %s
                Categoria: %s
                Status atual: %s
                Prioridade atual: %s

                Retorne APENAS um JSON válido, sem markdown, sem explicações fora do JSON,
                exatamente neste formato:

                {
                  "pontuacao": 0,
                  "prioridadeSugerida": "BAIXA",
                  "justificativa": "texto curto"
                }

                Regras:
                - pontuacao deve ser de 0 a 100.
                - prioridadeSugerida deve ser BAIXA, MEDIA ou ALTA.
                - justificativa deve explicar potencial de impacto, viabilidade e aderência estratégica.
                """.formatted(
                ideia.getTitulo(),
                ideia.getDescricao(),
                ideia.getCategoria(),
                ideia.getStatus(),
                ideia.getPrioridade()
        );

        String respostaIa = chamarGemini(prompt);

        try {
            String jsonLimpo = extrairJson(respostaIa);
            JsonNode node = objectMapper.readTree(jsonLimpo);

            Integer pontuacao = node.get("pontuacao").asInt();
            PrioridadeIdeia prioridadeSugerida =
                    PrioridadeIdeia.valueOf(node.get("prioridadeSugerida").asText().toUpperCase());
            String justificativa = node.get("justificativa").asText();

            ideia.setPontuacaoIa(pontuacao);
            ideia.setPrioridadeSugeridaIa(prioridadeSugerida);
            ideia.setJustificativaIa(justificativa);
            ideiaRepository.save(ideia);

            AnaliseIa analise = new AnaliseIa();
            analise.setIdeiaId(ideia.getId());
            analise.setPontuacao(pontuacao);
            analise.setPrioridadeSugerida(prioridadeSugerida);
            analise.setJustificativa(justificativa);
            analise.setDataAnalise(LocalDateTime.now());

            AnaliseIa salva = analiseIaRepository.save(analise);

            return new IaAnaliseIdeiaResponse(
                    ideia.getId(),
                    salva.getPontuacao(),
                    salva.getPrioridadeSugerida(),
                    salva.getJustificativa(),
                    salva.getDataAnalise()
            );

        } catch (Exception e) {
            throw new RuntimeException("Erro ao interpretar resposta da IA: " + respostaIa);
        }
    }

    private String chamarGemini(String prompt) {
        try {
            String url = "https://generativelanguage.googleapis.com/v1beta/models/"
                    + model
                    + ":generateContent?key="
                    + apiKey;

            Map<String, Object> body = Map.of(
                    "contents", List.of(
                            Map.of(
                                    "parts", List.of(
                                            Map.of("text", prompt)
                                    )
                            )
                    )
            );

            String jsonBody = objectMapper.writeValueAsString(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new RuntimeException(
                        "Erro na Gemini API. Status: " + response.statusCode() +
                                " | Resposta: " + response.body()
                );
            }

            JsonNode root = objectMapper.readTree(response.body());

            return root
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao chamar IA: " + e.getMessage());
        }
    }

    private String extrairJson(String texto) {
        int inicio = texto.indexOf("{");
        int fim = texto.lastIndexOf("}");

        if (inicio == -1 || fim == -1 || fim <= inicio) {
            throw new RuntimeException("Resposta da IA não contém JSON válido.");
        }

        return texto.substring(inicio, fim + 1);
    }
}