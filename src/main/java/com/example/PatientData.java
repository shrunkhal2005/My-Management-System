package com.example;

/**
 * Patient Data Model - Data transfer object for patient information
 */
public class PatientData {
    public long id;
    public String name;
    public int age;
    public String phone;
    public String department;
    public String doctor;
    public String priority;
    public String time;
    public String email;
    
    public PatientData() {
    }
    
    public PatientData(long id, String name, String department, String doctor, String time) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.doctor = doctor;
        this.time = time;
    }
}
