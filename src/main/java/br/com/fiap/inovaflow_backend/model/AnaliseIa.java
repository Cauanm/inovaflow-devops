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
@Document(collection = "analises_ia")
public class AnaliseIa {

    @Id
    private String id;

    private String ideiaId;

    private Integer pontuacao;

    private PrioridadeIdeia prioridadeSugerida;

    private String justificativa;

    private LocalDateTime dataAnalise;
}