package com.maciejp.rat.exception;

import com.maciejp.rat.response.ExceptionResponse;
import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    protected String message;

    protected HttpStatus httpStatus;

    public ApiException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ExceptionResponse buildResponseBody() {
        return new ExceptionResponse(message);
    }
}
