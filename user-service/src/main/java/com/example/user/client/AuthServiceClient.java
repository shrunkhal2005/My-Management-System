package com.example.user.client;

import com.example.user.model.TokenValidationResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthServiceClient {
    private final RestTemplate restTemplate;

    public AuthServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public TokenValidationResponse validateToken(String token) {
        return restTemplate.getForObject(
                "http://auth-service/auth/validate-token?token=" + token,
                TokenValidationResponse.class
        );
    }
}