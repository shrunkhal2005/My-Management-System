import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/jfx_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "shrunkhal@2005";
    private Connection connection;

    // Initialize connection
    public boolean connect() {
        try {
            // Try to load MySQL driver
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                System.out.println("✅ MySQL JDBC Driver loaded successfully");
            } catch (ClassNotFoundException e1) {
                System.out.println("⚠️  MySQL JDBC Driver not in classpath");
                System.out.println("📝 Running in OFFLINE mode (data not persisted to MySQL)");
                return false;
            }
            
            System.out.println("🔌 Attempting to connect to: " + DB_URL);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("✅ MySQL Connected Successfully!");
            System.out.println("📊 Connected to: " + DB_URL);
            createTablesIfNotExists();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Connection failed: " + e.getMessage());
            System.out.println("📋 SQLState: " + e.getSQLState());
            System.out.println("📋 Error Code: " + e.getErrorCode());
            e.printStackTrace();
            System.out.println("📝 Running in OFFLINE mode (data not persisted to MySQL)");
            return false;
        }
    }

    // Create patient table if not exists
    private void createTablesIfNotExists() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS patients (" +
                    "id VARCHAR(50) PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "age INT, " +
                    "phone VARCHAR(20), " +
                    "department VARCHAR(50), " +
                    "doctor VARCHAR(100), " +
                    "priority VARCHAR(20), " +
                    "time VARCHAR(10), " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";
            
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
            System.out.println("✅ Patients table created/verified");
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    // Add patient
    public String addPatient(String id, String name, int age, String phone, 
                             String department, String doctor, String priority) {
        if (connection == null) return "ERROR: Database connection is null";
        
        try {
            String sql = "INSERT INTO patients (id, name, age, phone, department, doctor, priority, time) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setInt(3, age);
            pstmt.setString(4, phone);
            pstmt.setString(5, department);
            pstmt.setString(6, doctor);
            pstmt.setString(7, priority);
            pstmt.setString(8, java.time.LocalTime.now().toString().substring(0, 5));
            
            int rows = pstmt.executeUpdate();
            pstmt.close();
            System.out.println("✅ Patient added: " + name);
            return "SUCCESS";
        } catch (SQLException e) {
            String errorMsg = "Error adding patient: " + e.getMessage();
            System.out.println("❌ " + errorMsg);
            e.printStackTrace();
            return "ERROR: " + e.getMessage();
        }
    }

    // Get all patients
    public List<PatientData> getAllPatients() {
        List<PatientData> patients = new ArrayList<>();
        if (connection == null) return patients;
        
        try {
            String sql = "SELECT id, name, department, doctor, time FROM patients ORDER BY created_at DESC";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                patients.add(new PatientData(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("department"),
                    rs.getString("doctor"),
                    rs.getString("time")
                ));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error fetching patients: " + e.getMessage());
        }
        return patients;
    }

    // Search patients by name, id, or department
    public List<PatientData> searchPatients(String searchText) {
        List<PatientData> patients = new ArrayList<>();
        if (connection == null) return patients;
        
        try {
            String sql = "SELECT id, name, department, doctor, time FROM patients " +
                    "WHERE id LIKE ? OR name LIKE ? OR department LIKE ? " +
                    "ORDER BY created_at DESC";
            
            PreparedStatement pstmt = connection.prepareStatement(sql);
            String searchParam = "%" + searchText + "%";
            pstmt.setString(1, searchParam);
            pstmt.setString(2, searchParam);
            pstmt.setString(3, searchParam);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                patients.add(new PatientData(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("department"),
                    rs.getString("doctor"),
                    rs.getString("time")
                ));
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error searching patients: " + e.getMessage());
        }
        return patients;
    }

    // Delete patient
    public boolean deletePatient(String id) {
        if (connection == null) return false;
        
        try {
            String sql = "DELETE FROM patients WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            pstmt.close();
            System.out.println("✅ Patient deleted: " + id);
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Error deleting patient: " + e.getMessage());
            return false;
        }
    }

    // Get patient count
    public int getPatientCount() {
        if (connection == null) return 0;
        
        try {
            String sql = "SELECT COUNT(*) as count FROM patients";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt("count");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error getting patient count: " + e.getMessage());
        }
        return 0;
    }

    // Close connection
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ MySQL Disconnected");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }

    // Check if connected
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}

// Data class for patient results
class PatientData {
    public String id;
    public String name;
    public String department;
    public String doctor;
    public String time;

    public PatientData(String id, String name, String department, String doctor, String time) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.doctor = doctor;
        this.time = time;
    }
}
