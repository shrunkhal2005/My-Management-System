package com.example.user.repository;

import com.example.user.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUsername(String username);
}