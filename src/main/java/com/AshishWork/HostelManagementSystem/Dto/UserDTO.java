package com.AshishWork.HostelManagementSystem.Dto;

import com.AshishWork.HostelManagementSystem.Enum.UserRole;
import lombok.Data;

@Data

public class UserDTO {

    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private UserRole role;

}
