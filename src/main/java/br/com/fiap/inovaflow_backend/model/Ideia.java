package br.com.fiap.inovaflow_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ideias")
public class Ideia {

    @Id
    private String id;

    private String titulo;
    private String descricao;
    private String categoria;

    private StatusIdeia status;
    private PrioridadeIdeia prioridade;

    private String operadorId;
    private String operadorNome;

    private String estrategiaId;

    private String observacaoGestor;

    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    private Integer pontuacaoIa;
    private PrioridadeIdeia prioridadeSugeridaIa;
    private String justificativaIa;
}