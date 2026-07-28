package com.AshishWork.HostelManagementSystem.Controller;

import com.AshishWork.HostelManagementSystem.Entity.HostelDocument;
import com.AshishWork.HostelManagementSystem.Service.HostelDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/documents")
public class HostelDocumentController {

    @Autowired
    private HostelDocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("module") String module,
            @RequestParam("username") String username) {

        Map<String, Object> response = new HashMap<>();

        if (file.isEmpty()) {
            response.put("success", false);
            response.put("message", "It is necessary to select a file.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            HostelDocument savedDoc = documentService.saveDocument(file, module, username);

            response.put("success", true);
            response.put("message", "The document has been successfully synced to the database and storage!");
            response.put("documentId", savedDoc.getId());
            response.put("fileName", savedDoc.getFileName());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Real-world storage processing failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<HostelDocument>> getUserDocuments(@PathVariable String username) {
        return ResponseEntity.ok(documentService.getDocumentsByUser(username));
    }

    @GetMapping("/check/{username}")
    public ResponseEntity<Map<String, Object>> checkDocumentStatus(@PathVariable String username) {
        Map<String, Object> response = new HashMap<>();
        List<HostelDocument> docs = documentService.getDocumentsByUser(username);

        if (docs != null && !docs.isEmpty()) {
            response.put("hasDocument", true);
            response.put("fileName", docs.get(0).getFileName());
        } else {
            response.put("hasDocument", false);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/view/{username}")
    public ResponseEntity<org.springframework.core.io.Resource> viewDocumentFile(@PathVariable String username) {
        try {
            List<HostelDocument> docs = documentService.getDocumentsByUser(username);
            if (docs == null || docs.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            HostelDocument doc = docs.get(0);
            java.io.File file = new java.io.File(doc.getFilePath());

            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            org.springframework.core.io.Resource resource = new org.springframework.core.io.UrlResource(file.toURI());

            String contentType = doc.getFileType();
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                    .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/view-by-id/{id}")
    public ResponseEntity<org.springframework.core.io.Resource> viewDocumentById(@PathVariable Long id) {
        try {
            HostelDocument doc = documentService.getDocumentById(id);

            java.io.File file = new java.io.File(doc.getFilePath());
            org.springframework.core.io.Resource resource = new org.springframework.core.io.UrlResource(file.toURI());

            return ResponseEntity.ok()
                    .contentType(org.springframework.http.MediaType.parseMediaType(doc.getFileType()))
                    .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + doc.getFileName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteDocument(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            documentService.deleteDocumentById(id);

            response.put("success", true);
            response.put("message", "Document deleted successfully!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to delete file from backend: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
