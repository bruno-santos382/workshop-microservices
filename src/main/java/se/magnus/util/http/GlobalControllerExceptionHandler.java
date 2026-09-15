package se.magnus.util.http;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import se.magnus.util.exceptions.InvalidInputException;
import se.magnus.util.exceptions.NotFoundException;

/**
 * Interceptador global de exceções da API.
 * Sempre que um Controller lançar uma exceção conhecida, essa classe captura o erro
 * e devolve uma resposta amigável em JSON com o formato HttpErrorInfo.
 */
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    /**
     * Trata a exceção NotFoundException retornando status HTTP 404 (Not Found).
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public HttpErrorInfo handleNotFoundException(HttpServletRequest request, NotFoundException ex) {
        return createHttpErrorInfo(HttpStatus.NOT_FOUND, request, ex);
    }

    /**
     * Trata a exceção InvalidInputException retornando status HTTP 422 (Unprocessable Entity).
     */
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidInputException.class)
    @ResponseBody
    public HttpErrorInfo handleInvalidInputException(HttpServletRequest request, InvalidInputException ex) {
        return createHttpErrorInfo(HttpStatus.UNPROCESSABLE_ENTITY, request, ex);
    }

    /**
     * Método auxiliar para criar o objeto padronizado de erro HttpErrorInfo e registrar no log.
     */
    private HttpErrorInfo createHttpErrorInfo(HttpStatus httpStatus, HttpServletRequest request, Exception ex) {
        final String path = request.getRequestURI();
        final String message = ex.getMessage();

        LOG.debug("Retornando status HTTP {}: {} na rota {}", httpStatus, message, path);
        return new HttpErrorInfo(httpStatus, path, message);
    }
}
