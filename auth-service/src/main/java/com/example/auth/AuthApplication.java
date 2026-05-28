package com.example.auth;

import com.example.auth.entity.AuthUser;
import com.example.auth.entity.UserProfile;
import com.example.auth.repository.AuthUserRepository;
import com.example.auth.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.auth.service.AccessTokenService;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    CommandLineRunner seedAuthUsers(AuthUserRepository repository, UserProfileRepository profileRepository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new AuthUser(null, "admin", "admin123", true));
                repository.save(new AuthUser(null, "alice", "password1", true));
            }

            if (profileRepository.count() == 0) {
                profileRepository.save(new UserProfile(null, "alice", "Alice Example", "alice@example.com"));
                profileRepository.save(new UserProfile(null, "bob", "Bob Example", "bob@example.com"));
            }
        };
    }

    @Bean
    AccessTokenService accessTokenService(
            @Value("${auth.jwt.secret}") String secret,
            @Value("${auth.jwt.ttl-seconds}") long ttlSeconds) {
        return new AccessTokenService(secret, ttlSeconds);
    }
}
