package com.maciejp.rat.exception;

import org.springframework.http.HttpStatus;

public abstract class ApiException extends RuntimeException {

    protected String message;

    protected HttpStatus httpStatus;

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
