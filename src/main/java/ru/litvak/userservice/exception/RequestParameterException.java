package ru.litvak.userservice.exception;

import lombok.Getter;

@Getter
public class RequestParameterException extends RuntimeException {
    private final String field;

    public RequestParameterException(String field, String message) {
        super(message);
        this.field = field;
    }
}
