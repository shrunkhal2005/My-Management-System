package com.example.auth;

import com.example.auth.entity.AuthUser;
import com.example.auth.repository.AuthUserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@SpringBootApplication
@RestController
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    @GetMapping("/")
    public Map<String, Object> home() {
        return Map.of(
                "service", "auth-service",
                "status", "running",
                "routes", Map.of(
                        "login", "/auth/login",
                        "validateToken", "/auth/validate-token?token=...",
                        "users", "/auth/users"
                )
        );
    }

    @Bean
    CommandLineRunner seedAuthUsers(AuthUserRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new AuthUser(null, "admin", "admin123", true));
                repository.save(new AuthUser(null, "alice", "password1", true));
            }
        };
    }
}
