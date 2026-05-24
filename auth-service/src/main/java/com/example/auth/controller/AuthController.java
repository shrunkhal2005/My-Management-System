package com.example.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");
        // Very simple credential check for scaffold/demo purposes
        if (username != null && password != null) {
            String token = "token-" + username + "-" + System.currentTimeMillis();
            Map<String, String> res = new HashMap<>();
            res.put("accessToken", token);
            res.put("tokenType", "Bearer");
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.status(400).body(Map.of("error", "invalid_credentials"));
    }
}
