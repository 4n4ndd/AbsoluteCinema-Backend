package com.absolutecinema.backend.dto;

public class LoginRequest {
    private String email;
    private String password;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
//    public LoginRequest(String token) {
//        this.token = token;
//    }

    public LoginRequest() {
    }

    public LoginRequest(String email, String password,String token) {
        this.email = email;
        this.password = password;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "email='" + email + '\'' +
                //", password='" + password + '\'' +
                '}';
    }
}