package com.AshishWork.HostelManagementSystem.Service;

import com.AshishWork.HostelManagementSystem.Dto.LoginRequest;
import com.AshishWork.HostelManagementSystem.Dto.RegisterRequest;
import com.AshishWork.HostelManagementSystem.Dto.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    String login(LoginRequest request);
    AuthResponse registerAdminFromBackend(RegisterRequest request);
}
