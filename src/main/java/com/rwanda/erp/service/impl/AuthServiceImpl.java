package com.rwanda.erp.service.impl;

import com.rwanda.erp.dto.JwtResponse;
import com.rwanda.erp.dto.LoginRequest;
import com.rwanda.erp.model.Employee;
import com.rwanda.erp.repository.EmployeeRepository;
import com.rwanda.erp.security.JwtTokenUtil;
import com.rwanda.erp.security.CustomUserDetailsService;
import com.rwanda.erp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService userDetailsService; // To load UserDetails after authentication

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Retrieve the employee to get the ID
        Employee employee = employeeRepository.findByEmail(loginRequest.getEmail())
                 .orElseThrow(() -> new RuntimeException("Employee not found after authentication")); // Should not happen if authentication successful

        return new JwtResponse(token, "Bearer", employee.getCode(), employee.getEmail());
    }
}