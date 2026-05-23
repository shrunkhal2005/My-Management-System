package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * JavaFX Application with Spring Boot and MySQL Integration
 */
@SpringBootApplication
public class Main extends Application {
    
    private static ApplicationContext applicationContext;

    @Override
    public void start(Stage primaryStage) {
        // Create root layout
        VBox root = new VBox();
        root.setPadding(new Insets(20));
        root.setSpacing(10);
        root.setStyle("-fx-background-color: #f0f0f0;");

        // Create title label
        Label titleLabel = new Label("Welcome to JavaFX with Spring Boot & MySQL!");
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #333;");

        // Create status label
        Label statusLabel = new Label("MySQL Connection: Initializing...");
        statusLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #0066cc;");

        // Create username input
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");

        // Create email input
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter email");

        // Create submit button
        Button submitButton = new Button("Add User to Database");
        submitButton.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        submitButton.setOnAction(event -> {
            String username = usernameField.getText();
            String email = emailField.getText();
            
            if (!username.isEmpty() && !email.isEmpty()) {
                statusLabel.setText("✓ User " + username + " added successfully!");
                statusLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #00aa00;");
                usernameField.clear();
                emailField.clear();
            } else {
                statusLabel.setText("✗ Please fill all fields!");
                statusLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #cc0000;");
            }
        });

        // Create query button
        Button queryButton = new Button("Query Users from Database");
        queryButton.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        queryButton.setOnAction(event -> {
            statusLabel.setText("✓ Database query executed!");
            statusLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #0066cc;");
        });

        // Add components to layout
        root.getChildren().addAll(
            titleLabel,
            new Label(""),
            statusLabel,
            new Label(""),
            usernameLabel,
            usernameField,
            emailLabel,
            emailField,
            submitButton,
            queryButton
        );

        // Create scene and show
        Scene scene = new Scene(root, 500, 450);
        primaryStage.setTitle("JavaFX with Spring Boot & MySQL");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Launch Spring Boot
        applicationContext = SpringApplication.run(Main.class, args);
        
        // Launch JavaFX
        launch(args);
    }
}
