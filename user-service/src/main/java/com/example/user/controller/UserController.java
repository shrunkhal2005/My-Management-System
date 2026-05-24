package com.example.user.controller;

import com.example.user.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@RestController
@RequestMapping("/users")
public class UserController {

    private final List<User> users = new ArrayList<>();

    public UserController() {
        users.add(new User(1L, "alice", "Alice Example"));
        users.add(new User(2L, "bob", "Bob Example"));
    }

    @GetMapping
    public ResponseEntity<List<User>> list() {
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable Long id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
