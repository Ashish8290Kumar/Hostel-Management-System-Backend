package com.AshishWork.HostelManagementSystem.Impl;

import com.AshishWork.HostelManagementSystem.Dto.LoginRequest;
import com.AshishWork.HostelManagementSystem.Dto.RegisterRequest;
import com.AshishWork.HostelManagementSystem.Dto.AuthResponse;
import com.AshishWork.HostelManagementSystem.Entity.Student;
import com.AshishWork.HostelManagementSystem.Entity.User;
import com.AshishWork.HostelManagementSystem.Enum.UserRole;
import com.AshishWork.HostelManagementSystem.Repositroy.StudentRepository;
import com.AshishWork.HostelManagementSystem.Repositroy.UserRepository;
import com.AshishWork.HostelManagementSystem.Service.AuthService;
import com.AshishWork.HostelManagementSystem.JWT.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private UserDetailsService userDetailsService;
    @Autowired private JwtUtils jwtUtils;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        if (request.getRole() == UserRole.ADMIN) {
            throw new RuntimeException("Admin registration from public interface is restricted!");
        }

        user.setRole(request.getRole());
        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        if (request.getRole() == UserRole.STUDENT) {
            Student student = new Student();
            student.setUser(savedUser);
            student.setRollNumber(request.getRollNumber());
            student.setFeeStatus("PENDING");
            studentRepository.save(student);
        }

        // ✅ Generate JWT token
        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getUsername());
        String token = jwtUtils.generateToken(userDetails);

        String assignedRole = userDetails.getAuthorities().stream()
                .map(granted -> granted.getAuthority())
                .findFirst().orElse("");

        return new AuthResponse(token, savedUser.getUsername(), assignedRole);
    }

    @Override
    public String login(LoginRequest request) {
        return "Handled by Controller Authentication Manager Engine";
    }

    @Override
    @Transactional
    public AuthResponse registerAdminFromBackend(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        user.setRole(UserRole.ADMIN);
        user.setEnabled(true);

        User savedAdmin = userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(savedAdmin.getUsername());
        String token = jwtUtils.generateToken(userDetails);

        String assignedRole = userDetails.getAuthorities().stream()
                .map(granted -> granted.getAuthority())
                .findFirst().orElse("");

        return new AuthResponse(token, savedAdmin.getUsername(), assignedRole);
    }
}
