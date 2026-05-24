package com.example.auth.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class UserServiceClient {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${user.service.base-url}")
    private String baseUrl;

    public Map<String, Object> getUserById(Long id) {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                baseUrl + "/users/" + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        return response.getBody();
    }
}