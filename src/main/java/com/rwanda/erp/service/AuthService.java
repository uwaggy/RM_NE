package com.rwanda.erp.service;

import com.rwanda.erp.dto.JwtResponse;
import com.rwanda.erp.dto.LoginRequest;
import com.rwanda.erp.dto.RegisterRequest;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);
    void registerUser(RegisterRequest registerRequest);
} 