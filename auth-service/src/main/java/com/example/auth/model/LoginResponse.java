package com.example.auth.model;

public class LoginResponse {
    private final String accessToken;
    private final String tokenType;
    private final String username;

    public LoginResponse(String accessToken, String tokenType, String username) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.username = username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getUsername() {
        return username;
    }
}