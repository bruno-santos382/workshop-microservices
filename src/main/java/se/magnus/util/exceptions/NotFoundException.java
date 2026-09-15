package se.magnus.util.exceptions;

/**
 * Exceção disparada quando um recurso solicitado não é encontrado no sistema.
 * Mapeada para retornar o status HTTP 404 (Not Found).
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }
}
