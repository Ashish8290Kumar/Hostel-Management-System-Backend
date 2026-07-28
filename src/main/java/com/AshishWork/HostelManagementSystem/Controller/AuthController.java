package com.AshishWork.HostelManagementSystem.Controller;

import com.AshishWork.HostelManagementSystem.Dto.LoginRequest;
import com.AshishWork.HostelManagementSystem.Dto.RegisterRequest;
import com.AshishWork.HostelManagementSystem.Dto.AuthResponse;
import com.AshishWork.HostelManagementSystem.JWT.JwtUtils;
import com.AshishWork.HostelManagementSystem.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AuthService authService;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private UserDetailsService userDetailsService;
    @Autowired private JwtUtils jwtUtils;


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String jwtToken = jwtUtils.generateToken(userDetails);

        String assignedRole = userDetails.getAuthorities().stream()
                .map(granted -> granted.getAuthority())
                .findFirst().orElse("");

        return ResponseEntity.ok(new AuthResponse(jwtToken, userDetails.getUsername(), assignedRole));
    }

    @PostMapping("/register-secure-admin-matrix")
    public ResponseEntity<AuthResponse> registerAdminMatrix(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.registerAdminFromBackend(request);
        return ResponseEntity.ok(response);
    }
}
