package com.AshishWork.HostelManagementSystem.Dto;

import com.AshishWork.HostelManagementSystem.Enum.UserRole;
import lombok.Data;

@Data
public class RegisterRequest {

    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private UserRole role;
    private String rollNumber;
}
