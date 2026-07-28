package com.AshishWork.HostelManagementSystem.Service;


import com.AshishWork.HostelManagementSystem.Entity.HostelDocument;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface HostelDocumentService {
    HostelDocument saveDocument(MultipartFile file, String module, String username) throws IOException;
    List<HostelDocument> getDocumentsByUser(String username);
    HostelDocument getDocumentById(Long id);
    void deleteDocumentById(Long id);


}
