package com.example.auth.model;

public class TokenValidationResponse {
    private final boolean valid;
    private final String username;

    public TokenValidationResponse(boolean valid, String username) {
        this.valid = valid;
        this.username = username;
    }

    public boolean isValid() {
        return valid;
    }

    public String getUsername() {
        return username;
    }
}