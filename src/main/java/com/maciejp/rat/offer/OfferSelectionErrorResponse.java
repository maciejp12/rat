package com.maciejp.rat.offer;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OfferSelectionErrorResponse {

    private final String message;

    public OfferSelectionErrorResponse(@JsonProperty("message") String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
