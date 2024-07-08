package com.example.ecommercespring.dto;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return username; // Assuming 'name' is synonymous with 'username'
    }
}
