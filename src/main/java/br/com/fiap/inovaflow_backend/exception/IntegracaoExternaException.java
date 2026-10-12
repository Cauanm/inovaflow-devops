package br.com.fiap.inovaflow_backend.exception;

public class IntegracaoExternaException extends RuntimeException {

    public IntegracaoExternaException(String mensagem) {
        super(mensagem);
    }

    public IntegracaoExternaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}