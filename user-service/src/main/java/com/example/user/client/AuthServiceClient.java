package com.example.user.client;

import com.example.user.model.TokenValidationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthServiceClient {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${auth.service.base-url}")
    private String baseUrl;

    public TokenValidationResponse validateToken(String token) {
        return restTemplate.getForObject(
                baseUrl + "/auth/validate-token?token=" + token,
                TokenValidationResponse.class
        );
    }
}