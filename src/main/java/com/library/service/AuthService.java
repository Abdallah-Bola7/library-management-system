package com.library.service;

import com.library.dto.auth.JwtResponse;
import com.library.dto.auth.LoginRequest;
import com.library.dto.auth.SignupRequest;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);
    JwtResponse registerUser(SignupRequest signupRequest);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
} 