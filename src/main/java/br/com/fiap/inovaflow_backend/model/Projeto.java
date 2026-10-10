package br.com.fiap.inovaflow_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "projetos")
public class Projeto {

    @Id
    private String id;

    private String nome;
    private String descricao;
    private String etapa;
    private StatusProjeto status;

    private Double investimento;
    private Double retornoFinanceiro;
    private Double lucroObtido;
    private Double roi;

    private Double aumentoProdutividade;

    private LocalDate prazo;

    private String estrategiaId;
    private String ideiaId;

    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}