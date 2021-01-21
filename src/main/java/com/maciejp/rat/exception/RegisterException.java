package com.maciejp.rat.exception;

import com.maciejp.rat.user.RegisterErrorResponse;
import org.springframework.http.HttpStatus;

public class RegisterException extends ApiException {

    public RegisterException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public RegisterErrorResponse buildResponse() {
        return new RegisterErrorResponse(message);
    }
}
