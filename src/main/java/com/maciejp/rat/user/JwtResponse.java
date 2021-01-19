package com.maciejp.rat.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JwtResponse {

    private final String token;

    private final String type = "Bearer";

    private final long id;

    private final String username;

    private final String email;

    public JwtResponse(@JsonProperty("token") String token,
                       @JsonProperty("id") long id,
                       @JsonProperty("username") String username,
                       @JsonProperty("email") String email) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
