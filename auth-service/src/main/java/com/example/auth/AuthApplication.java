package com.example.auth;

import com.example.auth.entity.AuthUser;
import com.example.auth.repository.AuthUserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
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
