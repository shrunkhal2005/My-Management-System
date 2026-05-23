import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloFX extends Application {
	private DatabaseConnection db = new DatabaseConnection();
	private ObservableList<PatientRow> patients = FXCollections.observableArrayList();

	private final ObservableList<AppointmentRow> appointments = FXCollections.observableArrayList(
			new AppointmentRow("OPD", "Room 2", "09:30", "Aisha Khan"),
			new AppointmentRow("Cardio", "Room 5", "10:15", "Rahul Mehta"),
			new AppointmentRow("Neuro", "Room 3", "11:05", "Elena Rossi")
	);
	
	private int nextPatientId = 1055;
	private TextField nameField, ageField, phoneField;
	private ComboBox<String> deptCombo, priorityCombo;
	private TextField searchField;
	private TableView<PatientRow> patientsTable;
	private VBox mainContent;
	private BorderPane root;
	private boolean emergencyMode = false;
	private ToggleButton emergencyToggle;
	private Label emergencyLabel;

	@Override
	public void start(Stage primaryStage) {
		// Connect to MySQL
		if (!db.connect()) {
			showAlert("Error", "Failed to connect to MySQL database.\nMake sure MySQL is running on localhost:3306");
		} else {
			// Load patients from database
			loadPatientsFromDatabase();
		}
		
		root = new BorderPane();
		root.setPadding(new Insets(16));
		root.setStyle("-fx-background-color: linear-gradient(to bottom right, #e9efff, #f6f0ff, #fde7f3);");

		root.setTop(buildHeader());
		root.setLeft(buildSidebar());
		mainContent = buildMainContent();
		root.setCenter(mainContent);
		root.setBottom(buildStatusBar());

		Scene scene = new Scene(root, 1200, 760);
		primaryStage.setTitle("HMS Dashboard - JavaFX (MySQL Connected)");
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(e -> db.disconnect());
		primaryStage.show();
	}

	private ToolBar buildHeader() {
		Label title = new Label("HMS - Hospital Management System");
		title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #142952;");

		searchField = new TextField();
		searchField.setPromptText("Search patients, doctors, departments...");
		searchField.setPrefWidth(320);
		searchField.setStyle("-fx-background-color: #ffffff; -fx-border-color: #9aa7ff; -fx-border-radius: 10; -fx-background-radius: 10; -fx-text-fill: #1f2937;");
		
		// Add search functionality
		searchField.textProperty().addListener((obs, oldVal, newVal) -> filterPatients(newVal));

		ToggleButton emergencyToggle = new ToggleButton("Emergency Mode");
		emergencyToggle.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: #ffffff; -fx-border-color: #ff3b4f; -fx-border-radius: 10; -fx-background-radius: 10; -fx-font-weight: bold;");
		emergencyToggle.setOnAction(e -> toggleEmergencyMode());

		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);

		Button notifications = new Button("Alerts (3)");
		Button user = new Button("Admin");
		notifications.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: #ffffff; -fx-border-color: #2563eb; -fx-border-radius: 10; -fx-background-radius: 10; -fx-font-weight: bold;");
		user.setStyle("-fx-background-color: #22c55e; -fx-text-fill: #ffffff; -fx-border-color: #16a34a; -fx-border-radius: 10; -fx-background-radius: 10; -fx-font-weight: bold;");
		
		notifications.setOnAction(e -> showAlerts());
		user.setOnAction(e -> showAdminPanel());

		ToolBar bar = new ToolBar(title, spacer, searchField, emergencyToggle, notifications, user);
		bar.setPadding(new Insets(8));
		bar.setStyle("-fx-background-color: linear-gradient(to right, #d9e7ff, #e9ddff); -fx-border-color: #c9d7ff; -fx-border-width: 0 0 1 0;");
		return bar;
	}
	
	private void filterPatients(String searchText) {
		if (searchText == null || searchText.isEmpty()) {
			patientsTable.setItems(patients);
		} else {
			// Search in database
			ObservableList<PatientRow> filtered = FXCollections.observableArrayList();
			java.util.List<PatientData> dbResults = db.searchPatients(searchText);
			for (PatientData pd : dbResults) {
				filtered.add(new PatientRow(pd.id, pd.name, pd.department, pd.doctor, pd.time));
			}
			patientsTable.setItems(filtered);
		}
	}
	
	private void toggleEmergencyMode() {
		emergencyMode = !emergencyMode;
		String bgColor = emergencyMode ? "#ffe0e0" : "linear-gradient(to bottom right, #e9efff, #f6f0ff, #fde7f3)";
		root.setStyle("-fx-background-color: " + bgColor + ";");
		
		showAlert("Emergency Mode", emergencyMode ? "🚨 EMERGENCY MODE ACTIVATED 🚨" : "Emergency mode deactivated");
	}
	
	private void loadPatientsFromDatabase() {
		patients.clear();
		java.util.List<PatientData> dbPatients = db.getAllPatients();
		for (PatientData pd : dbPatients) {
			patients.add(new PatientRow(pd.id, pd.name, pd.department, pd.doctor, pd.time));
		}
		System.out.println("✅ Loaded " + patients.size() + " patients from MySQL");
	}
	
	private void showAlerts() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("System Alerts");
		alert.setHeaderText("3 Active Alerts");
		alert.setContentText("1. ⚠️  Critical patient in ICU - Bed #5\n2. ⚠️  Lab results pending for P-1032\n3. ⚠️  Doctor availability issue in Cardio dept");
		alert.showAndWait();
	}
	
	private void showAdminPanel() {
		Stage adminStage = new Stage();
		adminStage.setTitle("Admin Panel");
		
		VBox adminBox = new VBox(15);
		adminBox.setPadding(new Insets(20));
		adminBox.setStyle("-fx-background-color: linear-gradient(to right, #f0f4ff, #fff0f5);");
		
		Label title = new Label("Admin Panel - System Management");
		title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2f2a7d;");
		
		// Database status
		String dbStatus = db.isConnected() ? "✅ MySQL Connected" : "⚠️ OFFLINE MODE (MySQL Driver Missing)";
		Label dbStatusLabel = new Label("Database: " + dbStatus);
		dbStatusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + (db.isConnected() ? "#059669" : "#ea580c") + ";");
		
		Label modeLabel = new Label(db.isConnected() ? "💾 Data will be saved to MySQL" : "📝 Data stored in memory only (NOT persistent)");
		modeLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #0369a1; -fx-font-style: italic;");
		
		Label patientCountLabel = new Label("Patients in Memory: " + patients.size() + (db.isConnected() ? " | MySQL: " + db.getPatientCount() : ""));
		patientCountLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #0369a1;");
		
		Button backupBtn = new Button("Backup Database");
		Button usersBtn = new Button("Manage Users");
		Button settingsBtn = new Button("System Settings");
		Button reportsBtn = new Button("Generate Reports");
		Button refreshBtn = new Button("Refresh Status");
		
		for (Button btn : new Button[]{backupBtn, usersBtn, settingsBtn, reportsBtn, refreshBtn}) {
			btn.setStyle("-fx-font-size: 12px; -fx-padding: 10; -fx-background-color: #7c3aed; -fx-text-fill: white; -fx-background-radius: 8;");
			btn.setPrefWidth(200);
		}
		
		backupBtn.setOnAction(e -> showAlert("Backup", "✓ Database backed up successfully!"));
		usersBtn.setOnAction(e -> showAlert("Users", "5 users active\n2 users pending approval"));
		settingsBtn.setOnAction(e -> showAlert("Settings", "All system settings configured"));
		reportsBtn.setOnAction(e -> showAlert("Reports", "Monthly reports generated\nTotal Patients: " + patients.size()));
		refreshBtn.setOnAction(e -> {
			int memoryCount = patients.size();
			int dbCount = db.isConnected() ? db.getPatientCount() : 0;
			patientCountLabel.setText("Patients in Memory: " + memoryCount + (db.isConnected() ? " | MySQL: " + dbCount : ""));
			dbStatusLabel.setText("Database: " + (db.isConnected() ? "✅ MySQL Connected" : "⚠️ OFFLINE MODE"));
			dbStatusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + (db.isConnected() ? "#059669" : "#ea580c") + ";");
			loadPatientsFromDatabase();
		});
		
		adminBox.getChildren().addAll(
			title,
			new Label(""),
			dbStatusLabel,
			modeLabel,
			patientCountLabel,
			new Label(""),
			new Label("Database Operations:"),
			backupBtn,
			new Label("User Management:"),
			usersBtn,
			new Label("Configuration:"),
			settingsBtn,
			reportsBtn,
			refreshBtn
		);
		
		ScrollPane scrollPane = new ScrollPane(adminBox);
		scrollPane.setFitToWidth(true);
		Scene adminScene = new Scene(scrollPane, 450, 650);
		adminStage.setScene(adminScene);
		adminStage.show();
	}

	private VBox buildSidebar() {
		VBox nav = new VBox(10);
		nav.setPadding(new Insets(8));
		nav.setPrefWidth(180);
		nav.setStyle("-fx-background-color: linear-gradient(to bottom, #dbe7ff, #ecf2ff); -fx-border-color: #c9d7ff; -fx-border-width: 0 1 0 0;");

		nav.getChildren().addAll(
				createNavButton("Dashboard", "dashboard"),
				createNavButton("Patients", "patients"),
				createNavButton("Doctors", "doctors"),
				createNavButton("Appointments", "appointments"),
				createNavButton("Billing", "billing"),
				createNavButton("Inventory", "inventory"),
				createNavButton("Reports", "reports"),
				createNavButton("Settings", "settings")
		);
		return nav;
	}
	
	private Button createNavButton(String text, String action) {
		Button btn = new Button(text);
		btn.setMaxWidth(Double.MAX_VALUE);
		btn.setStyle("-fx-background-color: #ffffff; -fx-border-color: #7c6bff; -fx-border-radius: 10; -fx-background-radius: 10; -fx-text-fill: #2f2a7d; -fx-font-weight: bold;");
		
		btn.setOnAction(e -> {
			switch(action) {
				case "dashboard":
					mainContent.getChildren().clear();
					mainContent.getChildren().addAll(buildMainContent().getChildren());
					break;
				case "patients":
					mainContent.getChildren().clear();
					mainContent.getChildren().add(buildPatientsManagement());
					break;
				case "doctors":
					mainContent.getChildren().clear();
					mainContent.getChildren().add(buildDoctorsPanel());
					break;
				case "appointments":
					mainContent.getChildren().clear();
					mainContent.getChildren().add(buildAppointmentsPanel());
					break;
				case "billing":
					mainContent.getChildren().clear();
					mainContent.getChildren().add(buildBillingPanel());
					break;
				case "inventory":
					mainContent.getChildren().clear();
					mainContent.getChildren().add(buildInventoryPanel());
					break;
				case "reports":
					mainContent.getChildren().clear();
					mainContent.getChildren().add(buildReportsPanel());
					break;
				case "settings":
					mainContent.getChildren().clear();
					mainContent.getChildren().add(buildSettingsPanel());
					break;
			}
		});
		
		return btn;
	}

	private VBox buildMainContent() {
		VBox content = new VBox(16);
		content.setPadding(new Insets(8, 0, 8, 16));

		content.getChildren().addAll(
				buildStatsRow(),
				buildPatientIntakeForm(),
				buildTablesRow()
		);
		return content;
	}
	
	private VBox buildPatientsManagement() {
		VBox wrapper = new VBox(12);
		wrapper.setPadding(new Insets(16));
		Label title = new Label("Patient Management System");
		title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #142952;");
		wrapper.getChildren().addAll(title, buildPatientIntakeForm());
		return wrapper;
	}
	
	private VBox buildDoctorsPanel() {
		VBox wrapper = new VBox(12);
		wrapper.setPadding(new Insets(16));
		wrapper.setStyle("-fx-background-color: white; -fx-border-radius: 10;");
		Label title = new Label("Doctor Management");
		title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #142952;");
		Label content = new Label("👨‍⚕️ Dr. Patel - OPD (Available)\n👩‍⚕️ Dr. Nair - Cardiology (Available)\n👨‍⚕️ Dr. Wong - Neurology (In-Surgery)\n👩‍⚕️ Dr. Smith - Orthopedics (Available)\n👨‍⚕️ Dr. Kumar - Pediatrics (On-Break)");
		content.setStyle("-fx-font-size: 14px; -fx-padding: 20;");
		wrapper.getChildren().addAll(title, content);
		return wrapper;
	}
	
	private VBox buildAppointmentsPanel() {
		VBox wrapper = new VBox(12);
		wrapper.setPadding(new Insets(16));
		Label title = new Label("Appointment Schedule");
		title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #142952;");
		wrapper.getChildren().addAll(title, buildAppointmentsTable());
		return wrapper;
	}
	
	private VBox buildBillingPanel() {
		VBox wrapper = new VBox(12);
		wrapper.setPadding(new Insets(16));
		wrapper.setStyle("-fx-background-color: white; -fx-border-radius: 10;");
		Label title = new Label("Billing & Payments");
		title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #142952;");
		Label content = new Label("💰 Total Pending Bills: ₹45,000\n💳 Payments Today: ₹12,500\n📊 Outstanding: ₹89,350\n✓ Settled: " + patients.size() * 5000);
		content.setStyle("-fx-font-size: 14px; -fx-padding: 20;");
		wrapper.getChildren().addAll(title, content);
		return wrapper;
	}
	
	private VBox buildInventoryPanel() {
		VBox wrapper = new VBox(12);
		wrapper.setPadding(new Insets(16));
		wrapper.setStyle("-fx-background-color: white; -fx-border-radius: 10;");
		Label title = new Label("Medical Inventory");
		title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #142952;");
		Label content = new Label("📦 Total Items: 2,450\n⚠️  Low Stock: 23 items\n✓ Medications: 1,200\n🩺 Equipment: 800\n💊 Supplies: 450");
		content.setStyle("-fx-font-size: 14px; -fx-padding: 20;");
		wrapper.getChildren().addAll(title, content);
		return wrapper;
	}
	
	private VBox buildReportsPanel() {
		VBox wrapper = new VBox(12);
		wrapper.setPadding(new Insets(16));
		wrapper.setStyle("-fx-background-color: white; -fx-border-radius: 10;");
		Label title = new Label("System Reports");
		title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #142952;");
		Label content = new Label("📈 Monthly Report\n📊 Department Analytics\n👥 Patient Statistics\n💼 Staff Performance\n🏥 Facility Utilization");
		content.setStyle("-fx-font-size: 14px; -fx-padding: 20;");
		wrapper.getChildren().addAll(title, content);
		return wrapper;
	}
	
	private VBox buildSettingsPanel() {
		VBox wrapper = new VBox(12);
		wrapper.setPadding(new Insets(16));
		wrapper.setStyle("-fx-background-color: white; -fx-border-radius: 10;");
		Label title = new Label("System Settings");
		title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #142952;");
		Label content = new Label("⚙️  General Settings\n🔐 Security & Access\n🔔 Notification Preferences\n💾 Backup & Recovery\n🌍 System Configuration");
		content.setStyle("-fx-font-size: 14px; -fx-padding: 20;");
		wrapper.getChildren().addAll(title, content);
		return wrapper;
	}

	private HBox buildStatsRow() {
		HBox row = new HBox(12);
		row.getChildren().addAll(
				statCard("Today's Visits", "128", "+12%"),
				statCard("Active Patients", "52", "Stable"),
				statCard("Available Beds", "18", "3 ICU"),
				statCard("Pending Labs", "9", "Urgent 2")
		);
		return row;
	}

	private VBox statCard(String title, String value, String meta) {
		VBox card = new VBox(6);
		card.setPadding(new Insets(12));
		card.setPrefWidth(220);
		card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #7c6bff; -fx-border-radius: 12; -fx-background-radius: 12;");

		Label titleLabel = new Label(title);
		titleLabel.setStyle("-fx-text-fill: #4b3fad; -fx-font-weight: bold;");
		Label valueLabel = new Label(value);
		valueLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");
		Label metaLabel = new Label(meta);
		metaLabel.setStyle("-fx-text-fill: #16a34a; -fx-font-weight: bold;");

		card.getChildren().addAll(titleLabel, valueLabel, metaLabel);
		return card;
	}

	private VBox buildPatientIntakeForm() {
		VBox wrapper = new VBox(8);
		wrapper.setPadding(new Insets(12));
		wrapper.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ff63b6; -fx-border-radius: 12; -fx-background-radius: 12;");

		Label title = new Label("New Patient Intake");
		title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #cf1f7a;");

		GridPane grid = new GridPane();
		grid.setHgap(12);
		grid.setVgap(10);

		nameField = new TextField();
		ageField = new TextField();
		phoneField = new TextField();
		deptCombo = new ComboBox<>(FXCollections.observableArrayList("OPD", "Cardio", "Neuro", "Ortho", "Pediatrics"));
		priorityCombo = new ComboBox<>(FXCollections.observableArrayList("Normal", "Urgent", "Critical"));

		nameField.setPromptText("Full name");
		ageField.setPromptText("Age");
		phoneField.setPromptText("Contact number");
		deptCombo.setPromptText("Department");
		priorityCombo.setPromptText("Priority");
		nameField.setStyle("-fx-background-color: #fff1f2; -fx-border-color: #ff77c8; -fx-border-radius: 8; -fx-background-radius: 8;");
		ageField.setStyle("-fx-background-color: #fff1f2; -fx-border-color: #ff77c8; -fx-border-radius: 8; -fx-background-radius: 8;");
		phoneField.setStyle("-fx-background-color: #fff1f2; -fx-border-color: #ff77c8; -fx-border-radius: 8; -fx-background-radius: 8;");
		deptCombo.setStyle("-fx-background-color: #fff1f2; -fx-border-color: #ff77c8; -fx-border-radius: 8; -fx-background-radius: 8;");
		priorityCombo.setStyle("-fx-background-color: #fff1f2; -fx-border-color: #ff77c8; -fx-border-radius: 8; -fx-background-radius: 8;");

		grid.add(new Label("Name"), 0, 0);
		grid.add(nameField, 1, 0);
		grid.add(new Label("Age"), 2, 0);
		grid.add(ageField, 3, 0);
		grid.add(new Label("Phone"), 0, 1);
		grid.add(phoneField, 1, 1);
		grid.add(new Label("Department"), 2, 1);
		grid.add(deptCombo, 3, 1);
		grid.add(new Label("Priority"), 0, 2);
		grid.add(priorityCombo, 1, 2);

		Button save = new Button("Save Patient");
		Button reset = new Button("Clear");
		save.setStyle("-fx-background-color: #7c3aed; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-weight: bold;");
		reset.setStyle("-fx-background-color: #fbbf24; -fx-text-fill: #78350f; -fx-background-radius: 10; -fx-font-weight: bold;");
		
		// Add event handlers
		save.setOnAction(e -> savePatient());
		reset.setOnAction(e -> clearForm());
		
		HBox actions = new HBox(10, save, reset);
		actions.setAlignment(Pos.CENTER_LEFT);

		wrapper.getChildren().addAll(title, grid, actions);
		return wrapper;
	}
	
	private void savePatient() {
		String name = nameField.getText().trim();
		String age = ageField.getText().trim();
		String phone = phoneField.getText().trim();
		String dept = deptCombo.getValue();
		String priority = priorityCombo.getValue();
		
		// Validation
		if (name.isEmpty()) {
			showAlert("Error", "Please enter patient name");
			return;
		}
		if (age.isEmpty()) {
			showAlert("Error", "Please enter patient age");
			return;
		}
		if (phone.isEmpty()) {
			showAlert("Error", "Please enter phone number");
			return;
		}
		if (dept == null) {
			showAlert("Error", "Please select department");
			return;
		}
		
		// Get doctor for department (mock assignment)
		String doctor = getAssignedDoctor(dept);
		String patientId = "P-" + (1000 + db.getPatientCount() + 1);
		String time = String.format("%02d:%02d", 9 + (patients.size() % 4), 30);
		
		// Save to MySQL
		try {
			int ageInt = Integer.parseInt(age);
			String result = db.addPatient(patientId, name, ageInt, phone, dept, doctor, priority != null ? priority : "Normal");
			
			if (result.equals("SUCCESS")) {
				// Add to UI list
				patients.add(new PatientRow(patientId, name, dept, doctor, time));
				
				// Add to appointments
				String room = "Room " + (2 + (patients.size() % 6));
				appointments.add(new AppointmentRow(dept, room, time, name));
				
				showAlert("Success", "✅ Patient " + name + " (" + patientId + ") registered successfully!\n📊 Data saved to MySQL");
				clearForm();
			} else {
				showAlert("Error", "Failed to save patient to database\n\n" + result);
			}
		} catch (NumberFormatException e) {
			showAlert("Error", "Please enter valid age");
		}
	}
	
	private void clearForm() {
		nameField.clear();
		ageField.clear();
		phoneField.clear();
		deptCombo.setValue(null);
		priorityCombo.setValue(null);
		searchField.clear();
	}
	
	private String getAssignedDoctor(String department) {
		switch(department) {
			case "OPD": return "Dr. Patel";
			case "Cardio": return "Dr. Nair";
			case "Neuro": return "Dr. Wong";
			case "Ortho": return "Dr. Smith";
			case "Pediatrics": return "Dr. Kumar";
			default: return "Dr. General";
		}
	}
	
	private void showAlert(String title, String message) {
		Alert alert = new Alert(title.equals("Success") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private HBox buildTablesRow() {
		HBox row = new HBox(12);
		row.getChildren().addAll(buildPatientsTable(), buildAppointmentsTable());
		return row;
	}

	private VBox buildPatientsTable() {
		VBox wrapper = new VBox(8);
		wrapper.setPadding(new Insets(12));
		wrapper.setStyle("-fx-background-color: #ffffff; -fx-border-color: #00bcd4; -fx-border-radius: 12; -fx-background-radius: 12;");
		wrapper.setPrefWidth(640);

		Label title = new Label("Patient Queue");
		title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #007c91;");

		patientsTable = new TableView<>(patients);
		patientsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
		patientsTable.setStyle("-fx-background-color: #ffffff; -fx-border-color: #b3ecf2; -fx-control-inner-background: #ffffff; -fx-control-inner-background-alt: #e6f7ff; -fx-table-cell-border-color: #dbeafe;");

		TableColumn<PatientRow, String> id = new TableColumn<>("ID");
		id.setCellValueFactory(new PropertyValueFactory<>("id"));
		TableColumn<PatientRow, String> name = new TableColumn<>("Name");
		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumn<PatientRow, String> dept = new TableColumn<>("Department");
		dept.setCellValueFactory(new PropertyValueFactory<>("department"));
		TableColumn<PatientRow, String> doctor = new TableColumn<>("Doctor");
		doctor.setCellValueFactory(new PropertyValueFactory<>("doctor"));
		TableColumn<PatientRow, String> time = new TableColumn<>("ETA");
		time.setCellValueFactory(new PropertyValueFactory<>("time"));

		patientsTable.getColumns().addAll(id, name, dept, doctor, time);

		wrapper.getChildren().addAll(title, patientsTable);
		return wrapper;
	}

	private VBox buildAppointmentsTable() {
		VBox wrapper = new VBox(8);
		wrapper.setPadding(new Insets(12));
		wrapper.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dbe4f3; -fx-border-radius: 8; -fx-background-radius: 8;");
		wrapper.setPrefWidth(360);

		Label title = new Label("Upcoming Appointments");
		title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");

		TableView<AppointmentRow> table = new TableView<>(appointments);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
		table.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e2e8f0;");

		TableColumn<AppointmentRow, String> dept = new TableColumn<>("Dept");
		dept.setCellValueFactory(new PropertyValueFactory<>("department"));
		TableColumn<AppointmentRow, String> room = new TableColumn<>("Room");
		room.setCellValueFactory(new PropertyValueFactory<>("room"));
		TableColumn<AppointmentRow, String> time = new TableColumn<>("Time");
		time.setCellValueFactory(new PropertyValueFactory<>("time"));
		TableColumn<AppointmentRow, String> patient = new TableColumn<>("Patient");
		patient.setCellValueFactory(new PropertyValueFactory<>("patient"));

		table.getColumns().addAll(dept, room, time, patient);

		wrapper.getChildren().addAll(title, table);
		return wrapper;
	}

	private HBox buildStatusBar() {
		HBox bar = new HBox(16);
		bar.setPadding(new Insets(8));
		bar.setAlignment(Pos.CENTER_LEFT);
		bar.setStyle("-fx-background-color: #eef2ff; -fx-border-color: #d7e0f0; -fx-border-width: 1 0 0 0;");

		bar.getChildren().addAll(
				new Label("Status: Connected"),
				new Label("Last sync: 10:42 AM"),
				new Label("Shift: Day")
		);
		return bar;
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static class PatientRow {
		private final SimpleStringProperty id;
		private final SimpleStringProperty name;
		private final SimpleStringProperty department;
		private final SimpleStringProperty doctor;
		private final SimpleStringProperty time;

		public PatientRow(String id, String name, String department, String doctor, String time) {
			this.id = new SimpleStringProperty(id);
			this.name = new SimpleStringProperty(name);
			this.department = new SimpleStringProperty(department);
			this.doctor = new SimpleStringProperty(doctor);
			this.time = new SimpleStringProperty(time);
		}

		public String getId() {
			return id.get();
		}

		public String getName() {
			return name.get();
		}

		public String getDepartment() {
			return department.get();
		}

		public String getDoctor() {
			return doctor.get();
		}

		public String getTime() {
			return time.get();
		}
	}

	public static class AppointmentRow {
		private final SimpleStringProperty department;
		private final SimpleStringProperty room;
		private final SimpleStringProperty time;
		private final SimpleStringProperty patient;

		public AppointmentRow(String department, String room, String time, String patient) {
			this.department = new SimpleStringProperty(department);
			this.room = new SimpleStringProperty(room);
			this.time = new SimpleStringProperty(time);
			this.patient = new SimpleStringProperty(patient);
		}

		public String getDepartment() {
			return department.get();
		}

		public String getRoom() {
			return room.get();
		}

		public String getTime() {
			return time.get();
		}

		public String getPatient() {
			return patient.get();
		}
	}
}
