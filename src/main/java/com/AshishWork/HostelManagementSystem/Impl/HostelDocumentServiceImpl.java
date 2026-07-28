package com.AshishWork.HostelManagementSystem.Impl;


import com.AshishWork.HostelManagementSystem.Entity.HostelDocument;
import com.AshishWork.HostelManagementSystem.Repositroy.HostelDocumentRepository;
import com.AshishWork.HostelManagementSystem.Service.HostelDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;


@Service
public class HostelDocumentServiceImpl implements HostelDocumentService {


        @Autowired
        private HostelDocumentRepository documentRepository;

        private static final String BASE_UPLOAD_DIR = System.getProperty("user.dir") + "/uploaded_documents/";

    @Override
    public HostelDocument saveDocument(MultipartFile file, String module, String username) throws IOException {


        String targetFolder = BASE_UPLOAD_DIR + module.toLowerCase() + "/";
        File directory = new File(targetFolder);
        if (!directory.exists()) { directory.mkdirs(); }


        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";
        String displayName = "Uploaded Document"; // Fallback name

        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

            displayName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
        } else if (originalFileName != null) {
            displayName = originalFileName;
        }


        String uniqueFileName = module.toLowerCase() + "_" + username + "_" + UUID.randomUUID().toString().substring(0, 8) + fileExtension;


        Path path = Paths.get(targetFolder + uniqueFileName);
        Files.write(path, file.getBytes());


        HostelDocument document = HostelDocument.builder()
                .fileName(uniqueFileName)
                .originalName(displayName)
                .fileType(file.getContentType())
                .filePath(path.toString())
                .module(module.toUpperCase())
                .uploadedBy(username)
                .build();

        return documentRepository.save(document);
    }


    @Override
        public List<HostelDocument> getDocumentsByUser(String username) {
            return documentRepository.findByUploadedBy(username);
        }

    @Override
    public HostelDocument getDocumentById(Long id) {
        return documentRepository.findById(id).orElseThrow(() -> new RuntimeException("File not found"));
    }


    @Override
    public void deleteDocumentById(Long id) {

        HostelDocument doc = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found in database with ID: " + id));


        try {
            if (doc.getFilePath() != null) {
                File fileOnDisk = new File(doc.getFilePath());
                if (fileOnDisk.exists()) {
                    boolean isDeleted = fileOnDisk.delete();
                    if (!isDeleted) {
                        System.out.println("Warning: Physical file could not be deleted from folder, checking database remove next.");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Storage system unlink error: " + e.getMessage());
        }


        documentRepository.delete(doc);
    }


}


