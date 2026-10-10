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
@Document(collection = "estrategias")
public class Estrategia {

    @Id
    private String id;

    private String titulo;
    private String descricao;
    private String categoria;
    private String campanha;

    private StatusEstrategia status;

    private LocalDateTime dataCriacao;
}