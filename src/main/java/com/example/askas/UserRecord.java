package com.example.askas;

import java.util.Map;

public class UserRecord {
    private String username;
    private String name;
    private String role;
    private Map<String, String> additionalData; // Contains all other data read from the file

    public UserRecord(String username, String name, String role, Map<String, String> additionalData) {
        this.username = username;
        this.name = name;
        this.role = role;
        this.additionalData = additionalData;
    }

    // Getters and setters
    public String getUsername() { return username; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public Map<String, String> getAdditionalData() { return additionalData; }

    @Override
    public String toString() {
        return "UserRecord{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", additionalData=" + additionalData +
                '}';
    }
}
