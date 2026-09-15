package se.magnus.util.exceptions;

/**
 * Exceção disparada quando os parâmetros ou dados fornecidos violam as regras da aplicação.
 * Exemplo: productId menor que 1 ou campos obrigatórios ausentes.
 * Mapeada para retornar o status HTTP 422 (Unprocessable Entity).
 */
public class InvalidInputException extends RuntimeException {

    public InvalidInputException() {
        super();
    }

    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidInputException(Throwable cause) {
        super(cause);
    }
}
