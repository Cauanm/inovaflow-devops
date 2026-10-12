package br.com.fiap.inovaflow_backend.exception;

import br.com.fiap.inovaflow_backend.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ApiErrorResponse> recursoNaoEncontrado(
            RecursoNaoEncontradoException ex,
            HttpServletRequest request
    ) {

        return criarResposta(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request,
                null
        );
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ApiErrorResponse> regraNegocio(
            RegraNegocioException ex,
            HttpServletRequest request
    ) {

        return criarResposta(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request,
                null
        );
    }

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<ApiErrorResponse> conflito(
            ConflitoException ex,
            HttpServletRequest request
    ) {

        return criarResposta(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request,
                null
        );
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<ApiErrorResponse> credenciaisInvalidas(
            CredenciaisInvalidasException ex,
            HttpServletRequest request
    ) {

        return criarResposta(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                request,
                null
        );
    }

    @ExceptionHandler(AcessoNegadoException.class)
    public ResponseEntity<ApiErrorResponse> acessoNegado(
            AcessoNegadoException ex,
            HttpServletRequest request
    ) {

        return criarResposta(
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                request,
                null
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> acessoNegadoSpring(
            AccessDeniedException ex,
            HttpServletRequest request
    ) {

        return criarResposta(
                HttpStatus.FORBIDDEN,
                "Você não possui permissão para acessar este recurso.",
                request,
                null
        );
    }

    @ExceptionHandler(IntegracaoExternaException.class)
    public ResponseEntity<ApiErrorResponse> integracaoExterna(
            IntegracaoExternaException ex,
            HttpServletRequest request
    ) {

        return criarResposta(
                HttpStatus.BAD_GATEWAY,
                ex.getMessage(),
                request,
                null
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> validacao(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {

        Map<String, String> campos =
                new LinkedHashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        campos.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return criarResposta(
                HttpStatus.BAD_REQUEST,
                "Existem campos inválidos na requisição.",
                request,
                campos
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> constraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request
    ) {

        return criarResposta(
                HttpStatus.BAD_REQUEST,
                "Um ou mais valores informados são inválidos.",
                request,
                null
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> jsonInvalido(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {

        return criarResposta(
                HttpStatus.BAD_REQUEST,
                "JSON inválido ou valor informado não é aceito.",
                request,
                null
        );
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ApiErrorResponse> chaveDuplicada(
            DuplicateKeyException ex,
            HttpServletRequest request
    ) {

        return criarResposta(
                HttpStatus.CONFLICT,
                "Já existe um registro com esses dados.",
                request,
                null
        );
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiErrorResponse> bancoIndisponivel(
            DataAccessException ex,
            HttpServletRequest request
    ) {

        return criarResposta(
                HttpStatus.SERVICE_UNAVAILABLE,
                "O banco de dados está temporariamente indisponível.",
                request,
                null
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> erroInterno(
            Exception ex,
            HttpServletRequest request
    ) {

        return criarResposta(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro interno no servidor.",
                request,
                null
        );
    }

    private ResponseEntity<ApiErrorResponse> criarResposta(
            HttpStatus status,
            String mensagem,
            HttpServletRequest request,
            Map<String, String> campos
    ) {

        ApiErrorResponse erro =
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        status.value(),
                        status.getReasonPhrase(),
                        mensagem,
                        request.getRequestURI(),
                        campos
                );

        return ResponseEntity
                .status(status)
                .body(erro);
    }
}