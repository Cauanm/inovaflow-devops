package br.com.fiap.inovaflow_backend.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiErrorResponse(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem,
        String path,
        Map<String, String> campos
) {
}