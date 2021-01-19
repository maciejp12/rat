package com.maciejp.rat.offer;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OfferCreationErrorResponse {

    private final String message;

    public OfferCreationErrorResponse(@JsonProperty("message") String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
