package com.example.auth.controller;

import com.example.auth.entity.AuthUser;
import com.example.auth.model.LoginRequest;
import com.example.auth.model.LoginResponse;
import com.example.auth.model.TokenValidationResponse;
import com.example.auth.repository.AuthUserRepository;
import com.example.auth.repository.UserProfileRepository;
import com.example.auth.service.AccessTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthUserRepository authUserRepository;
    private final UserProfileRepository userProfileRepository;
    private final AccessTokenService accessTokenService;

    public AuthController(AuthUserRepository authUserRepository, UserProfileRepository userProfileRepository, AccessTokenService accessTokenService) {
        this.authUserRepository = authUserRepository;
        this.userProfileRepository = userProfileRepository;
        this.accessTokenService = accessTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest payload) {
        if (payload.getUsername() == null || payload.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "missing_username_or_password"));
        }

        return authUserRepository.findByUsername(payload.getUsername())
            .filter(user -> user.isEnabled() && user.getPassword().equals(payload.getPassword()))
            .map(user -> ResponseEntity.ok((Object) new LoginResponse(
                accessTokenService.createToken(user.getUsername()),
                "Bearer",
                user.getUsername()
            )))
            .orElseGet(() -> ResponseEntity.status(401).body(Map.of("error", "invalid_credentials")));
    }

    @GetMapping("/validate-token")
    public ResponseEntity<TokenValidationResponse> validateToken(@RequestParam String token) {
        boolean valid = accessTokenService.isValid(token);
        String username = accessTokenService.extractUsername(token);
        return ResponseEntity.ok(new TokenValidationResponse(valid, username));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> proxyUser(@PathVariable Long id) {
        return userProfileRepository.findById(id)
            .map(user -> {
                Map<String, Object> profile = Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "displayName", user.getDisplayName(),
                    "email", user.getEmail(),
                    "source", "auth-service-local"
                );
                return ResponseEntity.ok(profile);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/users")
    public ResponseEntity<List<AuthUser>> listAuthUsers() {
        return ResponseEntity.ok(authUserRepository.findAll());
    }

    @PostMapping("/users")
    public ResponseEntity<?> createAuthUser(@RequestBody Map<String, Object> payload) {
        String username = payload.get("username") == null ? "" : String.valueOf(payload.get("username")).trim();
        String password = payload.get("password") == null ? "" : String.valueOf(payload.get("password")).trim();

        if (username.isBlank() || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "missing_username_or_password"));
        }

        if (authUserRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.status(409).body(Map.of("error", "username_already_exists"));
        }

        boolean enabled = true;
        if (payload.containsKey("enabled")) {
            enabled = Boolean.parseBoolean(String.valueOf(payload.get("enabled")));
        }

        AuthUser created = authUserRepository.save(new AuthUser(null, username, password, enabled));
        return ResponseEntity.ok(created);
    }

    @DeleteMapping("/users/auth/{id}")
    public ResponseEntity<?> deleteAuthUser(@PathVariable Long id) {
        if (!authUserRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        authUserRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("deleted", true, "id", id));
    }
}
