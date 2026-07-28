package com.AshishWork.HostelManagementSystem.Controller;

import com.AshishWork.HostelManagementSystem.Entity.Installment;
import com.AshishWork.HostelManagementSystem.Service.InstallmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")

public class InstallmentController {


        @Autowired
        private InstallmentService installmentService;

        // ⚡ ADMIN ACTION: Setup manual custom installment splits matrix
        @PostMapping("/admin/set-installments/{studentId}")
        public ResponseEntity<Map<String, Object>> setInstallments(
                @PathVariable Long studentId,
                @RequestBody List<Map<String, Object>> installmentList) {

            Map<String, Object> response = new HashMap<>();
            try {
                installmentService.createCustomInstallments(studentId, installmentList);
                response.put("success", true);
                response.put("message", "Total " + installmentList.size() + " custom installments deployed securely.");
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                response.put("success", false);
                response.put("message", "Failed to deploy installment structures: " + e.getMessage());
                return ResponseEntity.badRequest().body(response);
            }
        }

        // ⚡ STUDENT VIEW: Fetch sub-installment split array items list
        @GetMapping("/student/installments/{studentId}")
        public ResponseEntity<List<Installment>> getStudentInstallments(@PathVariable Long studentId) {
            try {
                List<Installment> list = installmentService.getInstallmentsByStudentId(studentId);
                return ResponseEntity.ok(list);
            } catch (Exception e) {
                return ResponseEntity.internalServerError().build();

        }
    }

}
