package com.example.hubspot_integration.exception;

/**
 * Exceção lançada quando ocorre uma falha de autorização, indicando que o acesso
 * ao recurso solicitado não foi permitido devido a credenciais ausentes ou inválidas.
 */
public class UnauthorizedException extends RuntimeException {

    /**
     * Construtor padrão com uma mensagem detalhada sobre a causa da exceção.
     *
     * @param message A mensagem de erro que descreve a falha de autorização.
     */
    public UnauthorizedException(String message) {
        super(message);
    }

    /**
     * Construtor que permite incluir a causa original da exceção.
     *
     * @param message A mensagem de erro que descreve a falha de autorização.
     * @param cause   A exceção que causou esta exceção.
     */
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}