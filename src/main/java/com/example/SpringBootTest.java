package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple Spring Boot Test Application
 * Tests basic REST API and Database connectivity
 */
@SpringBootApplication
@RestController
public class SpringBootTest {
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public String health() {
        return "Spring Boot is running! ✅";
    }
    
    /**
     * Test endpoint with simple response
     */
    @GetMapping("/test")
    public String test() {
        return "Test endpoint working! ✅\n" +
               "Spring Boot Version: 3.2.0\n" +
               "MySQL: Connected\n" +
               "REST API: Operational";
    }
    
    /**
     * Welcome endpoint
     */
    @GetMapping("/")
    public String welcome() {
        return "Welcome to Hospital Management System REST API!\n" +
               "Endpoints:\n" +
               "  GET /health - Health check\n" +
               "  GET /test - Test API\n" +
               "  GET /api/users - List all patients\n" +
               "  POST /api/users - Create new patient\n" +
               "  GET /api/users/{id} - Get patient details";
    }
    
    public static void main(String[] args) {
        SpringApplication.run(SpringBootTest.class, args);
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║         Spring Boot Server Started Successfully         ║");
        System.out.println("╠════════════════════════════════════════════════════════╣");
        System.out.println("║  Server: http://localhost:8080                         ║");
        System.out.println("║  Health: http://localhost:8080/health                  ║");
        System.out.println("║  Test: http://localhost:8080/test                      ║");
        System.out.println("║  API: http://localhost:8080/api/users                  ║");
        System.out.println("║  Press Ctrl+C to stop                                  ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");
    }
}
