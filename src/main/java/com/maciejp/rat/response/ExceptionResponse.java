package com.maciejp.rat.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExceptionResponse {

    private final String message;

    public ExceptionResponse(@JsonProperty("message") String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
