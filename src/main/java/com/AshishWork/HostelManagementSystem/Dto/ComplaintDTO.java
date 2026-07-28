package com.AshishWork.HostelManagementSystem.Dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ComplaintDTO {

    private Long id;
    private String studentName;
    private String rollNumber;
    private String title;
    private String description;
    private String status;
    private LocalDateTime createdAt;
}
