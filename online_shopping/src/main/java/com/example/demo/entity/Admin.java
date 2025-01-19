package com.example.demo.entity;

import jakarta.persistence.Entity;

@Entity
public class Admin extends User {

    private String role = "ADMIN";

    // Getters and Setters
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
