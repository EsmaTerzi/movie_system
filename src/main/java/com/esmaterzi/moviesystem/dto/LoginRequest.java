package com.esmaterzi.moviesystem.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    // 1. Parametresiz (Boş) Constructor - Jackson için şart!
    public LoginRequest() {
    }

    // 2. Parametreli Constructor - Testler için kolaylık olur
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // 3. Getter ve Setter'lar - Manuel olarak ekledik
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}