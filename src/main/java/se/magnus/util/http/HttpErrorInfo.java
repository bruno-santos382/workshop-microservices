package se.magnus.util.http;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

/**
 * Estrutura padrão para retornar detalhes de erros HTTP para os clientes da API.
 * Garante consistência nos retornos de exceções (como 404 e 422).
 */
public class HttpErrorInfo {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private final ZonedDateTime timestamp;
    private final String path;
    private final int status;
    private final String error;
    private final String message;

    // Construtor padrão vazio para serializadores
    public HttpErrorInfo() {
        this.timestamp = null;
        this.path = null;
        this.status = 0;
        this.error = null;
        this.message = null;
    }

    public HttpErrorInfo(HttpStatus httpStatus, String path, String message) {
        this.timestamp = ZonedDateTime.now();
        this.path = path;
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.message = message;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public String getPath() {
        return path;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
