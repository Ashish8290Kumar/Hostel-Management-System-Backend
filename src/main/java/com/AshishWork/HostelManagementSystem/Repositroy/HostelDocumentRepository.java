package com.AshishWork.HostelManagementSystem.Repositroy;

import com.AshishWork.HostelManagementSystem.Entity.HostelDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HostelDocumentRepository extends JpaRepository<HostelDocument, Long> {

    List<HostelDocument> findByUploadedBy(String uploadedBy);


    List<HostelDocument> findByModule(String module);

}
