package com.example.user;

import com.example.user.entity.UserProfile;
import com.example.user.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
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
