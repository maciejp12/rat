package com.maciejp.rat.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserSelectionErrorResponse {

    private final String message;

    public UserSelectionErrorResponse(@JsonProperty("message") String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
