package com.example.auth.controller;

import com.example.auth.client.UserServiceClient;
import com.example.auth.entity.AuthUser;
import com.example.auth.model.LoginRequest;
import com.example.auth.model.LoginResponse;
import com.example.auth.model.TokenValidationResponse;
import com.example.auth.repository.AuthUserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthUserRepository authUserRepository;
    private final UserServiceClient userServiceClient;

    public AuthController(AuthUserRepository authUserRepository, UserServiceClient userServiceClient) {
        this.authUserRepository = authUserRepository;
        this.userServiceClient = userServiceClient;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest payload) {
        if (payload.getUsername() == null || payload.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "missing_username_or_password"));
        }

        return authUserRepository.findByUsername(payload.getUsername())
                .filter(user -> user.isEnabled() && user.getPassword().equals(payload.getPassword()))
                .map(user -> ResponseEntity.ok(new LoginResponse(
                        "token-" + user.getUsername() + "-" + System.currentTimeMillis(),
                        "Bearer",
                        user.getUsername()
                )))
                .orElse(ResponseEntity.status(401).body(Map.of("error", "invalid_credentials")));
    }

    @GetMapping("/validate-token")
    public ResponseEntity<TokenValidationResponse> validateToken(@RequestParam String token) {
        boolean valid = token != null && token.startsWith("token-");
        String username = valid ? token.replaceFirst("token-", "").split("-")[0] : null;
        return ResponseEntity.ok(new TokenValidationResponse(valid, username));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> proxyUser(@PathVariable Long id) {
        Map<String, Object> user = userServiceClient.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users")
    public ResponseEntity<List<AuthUser>> listAuthUsers() {
        return ResponseEntity.ok(authUserRepository.findAll());
    }
}
