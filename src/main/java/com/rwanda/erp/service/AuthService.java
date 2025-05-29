package com.rwanda.erp.service;

import com.rwanda.erp.dto.JwtResponse;
import com.rwanda.erp.dto.LoginRequest;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);
} 