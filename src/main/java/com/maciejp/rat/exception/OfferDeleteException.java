package com.maciejp.rat.exception;

import com.maciejp.rat.offer.OfferDeleteErrorResponse;
import org.springframework.http.HttpStatus;

public class OfferDeleteException extends ApiException {

    public OfferDeleteException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public OfferDeleteErrorResponse buildResponse() {
        return new OfferDeleteErrorResponse(message);
    }
}
