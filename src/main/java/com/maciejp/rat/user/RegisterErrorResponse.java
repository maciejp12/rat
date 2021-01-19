package com.maciejp.rat.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterErrorResponse {

    private final String message;

    public RegisterErrorResponse(@JsonProperty("message") String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
