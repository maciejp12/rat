package com.maciejp.rat.exception;

import com.maciejp.rat.user.UserSelectionErrorResponse;
import org.springframework.http.HttpStatus;

public class UserSelectionException extends ApiException {

    public UserSelectionException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public UserSelectionErrorResponse buildResponse() {
        return new UserSelectionErrorResponse(message);
    }
}
