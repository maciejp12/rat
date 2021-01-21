package com.maciejp.rat.exception;

import com.maciejp.rat.offer.OfferCreationErrorResponse;
import org.springframework.http.HttpStatus;

public class OfferCreationException extends ApiException {

    public OfferCreationException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public OfferCreationErrorResponse buildResponse() {
        return new OfferCreationErrorResponse(message);
    }
}
