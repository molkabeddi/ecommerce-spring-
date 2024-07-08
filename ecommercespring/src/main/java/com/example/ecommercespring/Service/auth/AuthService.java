package com.example.ecommercespring.Service.auth;

import com.example.ecommercespring.dto.SignUpRequest;
import com.example.ecommercespring.dto.UserDto;

public interface AuthService {
    UserDto createUser(SignUpRequest signupRequest);

    boolean hasUserWithEmail(String email);
}
