package com.maciejp.rat.exception;

import com.maciejp.rat.offer.OfferSelectionErrorResponse;
import org.springframework.http.HttpStatus;

public class OfferSelectionException extends ApiException {

    public OfferSelectionException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public OfferSelectionErrorResponse buildResponse() {
        return new OfferSelectionErrorResponse(message);
    }
}
