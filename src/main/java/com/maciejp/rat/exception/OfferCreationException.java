package com.maciejp.rat.exception;

import com.maciejp.rat.offer.OfferCreationErrorResponse;
import org.springframework.http.HttpStatus;

public class OfferCreationException extends RuntimeException {

    private final String message;

    private final HttpStatus httpStatus;

    public OfferCreationException(String message, HttpStatus httpStatus) {
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


    public OfferCreationErrorResponse buildResponse() {
        return new OfferCreationErrorResponse(message);
    }
}
