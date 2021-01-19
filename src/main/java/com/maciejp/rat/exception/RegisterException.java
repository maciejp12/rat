package com.maciejp.rat.exception;

import com.maciejp.rat.user.RegisterErrorResponse;
import org.springframework.http.HttpStatus;

public class RegisterException extends RuntimeException {

    private final String message;

    private final HttpStatus httpStatus;

    public RegisterException(String message, HttpStatus httpStatus) {
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

    public RegisterErrorResponse buildResponse() {
        return new RegisterErrorResponse(message);
    }
}
