package br.com.leandro.crud.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandlers {

    @ExceptionHandler(ResourceNotFoundException.class)
    public Mono<ResponseEntity<ResponseErrorMessage>> resourceNotFoundExceptionExceptionHandler(Exception ex) {
        log.error("Exception: " + ex.getMessage(), ex);
        return Mono.just(ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ResponseErrorMessage
                                .builder()
                                .message(ex.getMessage())
                                .cause(ex.getCause())
                                .stackTrace(ex.getStackTrace())
                                .build()));
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String serverExceptionHandler(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ex.getMessage();
    }
}
