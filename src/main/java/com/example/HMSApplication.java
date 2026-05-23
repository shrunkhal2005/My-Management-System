package com.example;

import javafx.application.Application;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;

/**
 * Hospital Management System - Integrated with Spring Boot REST API
 * This version communicates with the REST API instead of direct database access
 */
public class HMSApplication extends Application {
    
    private RestApiClient restApiClient;
    private ObservableList<PatientData> patientsList;
    private TableView<PatientData> patientsTable;
    private Label connectionStatusLabel;
    
    @Override
    public void start(Stage primaryStage) {
        // Initialize REST API client
        restApiClient = new RestApiClient();
        patientsList = FXCollections.observableArrayList();
        
        // Create main layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(16));
        root.setStyle("-fx-background-color: #f5f5f5;");
        
        root.setTop(buildHeader());
        root.setCenter(buildMainContent());
        root.setBottom(buildStatusBar());
        
        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setTitle("HMS - Hospital Management System (REST API Integration)");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Load initial data
        loadAllPatients();
    }
    
    private HBox buildHeader() {
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #2c3e50; -fx-border-color: #34495e; -fx-border-width: 0 0 2 0;");
        
        Label title = new Label("🏥 Hospital Management System");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        connectionStatusLabel = new Label("Connecting to API...");
        connectionStatusLabel.setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
        updateConnectionStatus();
        
        Button refreshBtn = new Button("🔄 Refresh");
        refreshBtn.setStyle("-fx-padding: 8; -fx-background-color: #3498db; -fx-text-fill: white; -fx-border-radius: 5;");
        refreshBtn.setOnAction(e -> loadAllPatients());
        
        header.getChildren().addAll(title, spacer, connectionStatusLabel, refreshBtn);
        return header;
    }
    
    private void updateConnectionStatus() {
        if (restApiClient.isConnected()) {
            connectionStatusLabel.setText("✅ Connected to REST API (Port 8080)");
            connectionStatusLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
        } else {
            connectionStatusLabel.setText("❌ REST API Not Available - Retrying...");
            connectionStatusLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
            restApiClient.checkConnection();
        }
    }
    
    private VBox buildMainContent() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(16));
        
        // Add patient form
        content.getChildren().add(buildAddPatientForm());
        
        // Add patients table
        content.getChildren().add(new Label("Patients Database:"));
        content.getChildren().add(buildPatientsTable());
        
        return content;
    }
    
    private VBox buildAddPatientForm() {
        VBox form = new VBox(10);
        form.setPadding(new Insets(15));
        form.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-border-width: 1;");
        
        Label formTitle = new Label("➕ Add New Patient");
        formTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        // Form fields
        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");
        
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        
        TextField passwordField = new PasswordField();
        passwordField.setPromptText("Password (optional)");
        
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Username:"), 0, 2);
        grid.add(usernameField, 1, 2);
        grid.add(new Label("Password:"), 0, 3);
        grid.add(passwordField, 1, 3);
        
        // Submit button
        Button submitBtn = new Button("✅ Add Patient");
        submitBtn.setStyle("-fx-padding: 10; -fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5;");
        submitBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            
            if (name.isEmpty() || email.isEmpty() || username.isEmpty()) {
                showAlert("Error", "Please fill all required fields");
                return;
            }
            
            boolean success = restApiClient.createUser(username, name, email, password);
            if (success) {
                showAlert("Success", "✅ Patient added successfully!");
                nameField.clear();
                emailField.clear();
                usernameField.clear();
                passwordField.clear();
                loadAllPatients();
            } else {
                showAlert("Error", "Failed to add patient. Please check the API connection.");
            }
        });
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().add(submitBtn);
        
        form.getChildren().addAll(formTitle, grid, buttonBox);
        return form;
    }
    
    private ScrollPane buildPatientsTable() {
        patientsTable = new TableView<>();
        patientsTable.setStyle("-fx-font-size: 12;");
        
        TableColumn<PatientData, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleLongProperty(cellData.getValue().id).asObject());
        idCol.setPrefWidth(60);
        
        TableColumn<PatientData, String> nameCol = new TableColumn<>("Full Name");
        nameCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().name));
        nameCol.setPrefWidth(150);
        
        TableColumn<PatientData, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().email));
        emailCol.setPrefWidth(150);
        
        TableColumn<PatientData, String> doctorCol = new TableColumn<>("Doctor/Username");
        doctorCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().doctor));
        doctorCol.setPrefWidth(150);
        
        TableColumn<PatientData, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setPrefWidth(150);
        actionsCol.setCellFactory(param -> new TableCell<PatientData, Void>() {
            private final Button deleteBtn = new Button("🗑️ Delete");
            
            {
                deleteBtn.setStyle("-fx-padding: 5; -fx-background-color: #e74c3c; -fx-text-fill: white; -fx-border-radius: 3;");
                deleteBtn.setOnAction(event -> {
                    PatientData patient = getTableView().getItems().get(getIndex());
                    boolean success = restApiClient.deleteUser(patient.id);
                    if (success) {
                        showAlert("Success", "✅ Patient deleted successfully!");
                        loadAllPatients();
                    } else {
                        showAlert("Error", "Failed to delete patient");
                    }
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteBtn);
                }
            }
        });
        
        patientsTable.getColumns().addAll(idCol, nameCol, emailCol, doctorCol, actionsCol);
        patientsTable.setItems(patientsList);
        
        ScrollPane scrollPane = new ScrollPane(patientsTable);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: white;");
        return scrollPane;
    }
    
    private HBox buildStatusBar() {
        HBox statusBar = new HBox(15);
        statusBar.setPadding(new Insets(10));
        statusBar.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-width: 1 0 0 0;");
        
        Label patientCountLabel = new Label("Total Patients: " + patientsList.size());
        patientCountLabel.setStyle("-fx-font-weight: bold;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label apiLabel = new Label("REST API: http://localhost:8080/api/users");
        apiLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #7f8c8d;");
        
        patientsList.addListener((javafx.collections.ListChangeListener<? super PatientData>) c -> {
            patientCountLabel.setText("Total Patients: " + patientsList.size());
        });
        
        statusBar.getChildren().addAll(patientCountLabel, spacer, apiLabel);
        return statusBar;
    }
    
    private void loadAllPatients() {
        updateConnectionStatus();
        patientsList.clear();
        if (restApiClient.isConnected()) {
            List<PatientData> patients = restApiClient.getAllUsers();
            patientsList.addAll(patients);
            System.out.println("✅ Loaded " + patients.size() + " patients from REST API");
        } else {
            showAlert("Connection Error", "Cannot connect to REST API on port 8080.\nMake sure Spring Boot is running:\njava -jar target/jfx-app-1.0.jar");
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
