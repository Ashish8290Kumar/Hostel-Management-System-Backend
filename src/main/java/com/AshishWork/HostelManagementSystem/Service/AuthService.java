package com.AshishWork.HostelManagementSystem.Service;

import com.AshishWork.HostelManagementSystem.Dto.LoginRequest;
import com.AshishWork.HostelManagementSystem.Dto.RegisterRequest;

public interface AuthService {
    String register(RegisterRequest request);
    String login(LoginRequest request);
    String registerAdminFromBackend(com.AshishWork.HostelManagementSystem.Dto.RegisterRequest request);

}
