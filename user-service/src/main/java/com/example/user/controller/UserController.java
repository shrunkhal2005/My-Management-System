package com.example.user.controller;

import com.example.user.client.AuthServiceClient;
import com.example.user.entity.UserProfile;
import com.example.user.model.TokenValidationResponse;
import com.example.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final AuthServiceClient authServiceClient;

    public UserController(UserRepository userRepository, AuthServiceClient authServiceClient) {
        this.userRepository = userRepository;
        this.authServiceClient = authServiceClient;
    }

    @GetMapping
    public ResponseEntity<List<UserProfile>> list() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> get(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<UserProfile> getByUsername(@PathVariable String username) {
        return userRepository.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/validate-token")
    public ResponseEntity<TokenValidationResponse> validateToken(@RequestParam String token) {
        TokenValidationResponse response = authServiceClient.validateToken(token);
        if (response == null) {
            return ResponseEntity.status(502).build();
        }
        return ResponseEntity.ok(response);
    }
}
