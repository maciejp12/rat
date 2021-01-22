package com.maciejp.rat.offer;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OfferDeleteErrorResponse {

    private final String message;

    public OfferDeleteErrorResponse(@JsonProperty("message") String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
