package com.AshishWork.HostelManagementSystem.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "hostel_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostelDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileType;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private String module;

    @Column(nullable = false)
    private String uploadedBy;

    private LocalDateTime uploadTime;

    @PrePersist
    protected void onCreate() {
        this.uploadTime = LocalDateTime.now();
    }

    @Column(name = "original_name", length = 100)
    private String originalName;

}
