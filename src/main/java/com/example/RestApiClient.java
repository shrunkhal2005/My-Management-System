package com.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * REST API Client for Hospital Management System
 * Communicates with Spring Boot API on localhost:8080
 */
public class RestApiClient {
    private static final String API_BASE_URL = "http://localhost:8080/api/users";
    private static final String HEALTH_URL = "http://localhost:8080/health";
    private HttpClient httpClient;
    private boolean isConnected = false;

    public RestApiClient() {
        this.httpClient = HttpClient.newHttpClient();
        checkConnection();
    }

    /**
     * Check if REST API is reachable
     */
    public void checkConnection() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(HEALTH_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            isConnected = (response.statusCode() == 200);
            System.out.println(isConnected ? "✅ REST API Connected" : "❌ REST API Not Available");
        } catch (Exception e) {
            isConnected = false;
            System.out.println("❌ REST API Connection Failed: " + e.getMessage());
        }
    }

    /**
     * Get all users from REST API
     */
    public List<PatientData> getAllUsers() {
        List<PatientData> users = new ArrayList<>();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                JSONArray jsonArray = new JSONArray(response.body());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    PatientData patient = new PatientData();
                    patient.id = json.getLong("id");
                    patient.name = json.getString("fullName");
                    patient.email = json.getString("email");
                    patient.department = "General";
                    patient.doctor = json.getString("username");
                    users.add(patient);
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching users: " + e.getMessage());
        }
        return users;
    }

    /**
     * Create a new user via REST API
     */
    public boolean createUser(String username, String fullName, String email, String password) {
        try {
            JSONObject json = new JSONObject();
            json.put("username", username);
            json.put("fullName", fullName);
            json.put("email", email);
            json.put("password", password != null ? password : "secure123");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 201 || response.statusCode() == 200;
        } catch (Exception e) {
            System.err.println("Error creating user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update a user via REST API
     */
    public boolean updateUser(long id, String username, String fullName, String email, String password) {
        try {
            JSONObject json = new JSONObject();
            json.put("id", id);
            json.put("username", username);
            json.put("fullName", fullName);
            json.put("email", email);
            json.put("password", password != null ? password : "secure123");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + "/" + id))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete a user via REST API
     */
    public boolean deleteUser(long id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + "/" + id))
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 204 || response.statusCode() == 200;
        } catch (Exception e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Search users via REST API
     */
    public List<PatientData> searchUsers(String searchText) {
        List<PatientData> results = new ArrayList<>();
        try {
            // Get all users and filter locally
            List<PatientData> allUsers = getAllUsers();
            String searchLower = searchText.toLowerCase();
            
            for (PatientData user : allUsers) {
                if (user.name.toLowerCase().contains(searchLower) ||
                    user.doctor.toLowerCase().contains(searchLower) ||
                    String.valueOf(user.id).contains(searchLower)) {
                    results.add(user);
                }
            }
        } catch (Exception e) {
            System.err.println("Error searching users: " + e.getMessage());
        }
        return results;
    }

    /**
     * Check if REST API is connected
     */
    public boolean isConnected() {
        return isConnected;
    }
}
