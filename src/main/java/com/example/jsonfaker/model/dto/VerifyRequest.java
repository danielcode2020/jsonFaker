package com.example.jsonfaker.model.dto;

public class VerifyRequest {
    private String username;
    private String code;

    public VerifyRequest(String username, String code) {
        this.username = username;
        this.code = code;
    }

    public VerifyRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
