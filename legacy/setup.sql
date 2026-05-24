CREATE DATABASE IF NOT EXISTS jfx_db;
USE jfx_db;
CREATE TABLE IF NOT EXISTS patients (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT,
    phone VARCHAR(20),
    department VARCHAR(50),
    doctor VARCHAR(100),
    priority VARCHAR(20),
    time VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
SHOW TABLES;
SELECT COUNT(*) as patients_count FROM patients;
