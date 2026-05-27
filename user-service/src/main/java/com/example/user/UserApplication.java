package com.example.user;

import com.example.user.entity.UserProfile;
import com.example.user.repository.UserRepository;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@SpringBootApplication
@RestController
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @GetMapping("/")
    public Map<String, Object> home() {
        return Map.of(
                "service", "user-service",
                "status", "running",
                "routes", Map.of(
                        "users", "/users",
                        "byUsername", "/users/by-username/{username}",
                        "validateToken", "/users/validate-token?token=..."
                )
        );
    }

    @Bean
    CommandLineRunner seedUsers(UserRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new UserProfile(null, "alice", "Alice Example", "alice@example.com"));
                repository.save(new UserProfile(null, "bob", "Bob Example", "bob@example.com"));
            }
        };
    }
}
