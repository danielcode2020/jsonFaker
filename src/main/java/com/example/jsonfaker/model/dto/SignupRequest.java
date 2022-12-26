package com.example.jsonfaker.model.dto;

public class SignupRequest {
    private String username;
    private String password;

    public SignupRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public SignupRequest() {
    }
}
