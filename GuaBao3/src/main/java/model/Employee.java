package model;

import java.sql.Timestamp;

public class Employee {
    private String employeeId;
    private String password;
    private String name;
    private String role; // "最高權限" 或 "一般權限"
    private Timestamp createdAt;

    public Employee() {}

    public Employee(String employeeId, String password, String name, String role) {
        this.employeeId = employeeId;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    // Getters and Setters
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}