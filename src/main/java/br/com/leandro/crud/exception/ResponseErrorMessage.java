package br.com.leandro.crud.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseErrorMessage {
    String message;
    Throwable cause;
    StackTraceElement[] stackTrace;
}
