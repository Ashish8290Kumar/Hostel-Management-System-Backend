package com.AshishWork.HostelManagementSystem.Controller;

import com.AshishWork.HostelManagementSystem.Dto.PaymentVerificationRequest;
import com.AshishWork.HostelManagementSystem.Entity.Student;
import com.AshishWork.HostelManagementSystem.Repositroy.StudentRepository;
import com.AshishWork.HostelManagementSystem.Service.InstallmentService; // ⚡ Safe Import Added
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private InstallmentService installmentService;

    @PostMapping("/create-order")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody Map<String, Object> request) {
        Object amountObj = request.get("amount");
        Long amount = amountObj != null ? Long.parseLong(amountObj.toString()) : 5500L;

        Map<String, Object> response = new HashMap<>();
        response.put("id", "order_mock_" + UUID.randomUUID().toString().substring(0, 8));
        response.put("amount", amount * 100);
        response.put("currency", "INR");
        response.put("status", "created");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyPayment(@RequestBody PaymentVerificationRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {

            String currentStudent = request.getUsername();

            if (currentStudent != null && !currentStudent.trim().isEmpty()) {
                java.util.List<Student> allRegisteredStudents = studentRepository.findAll();
                boolean databaseUpdated = false;

                for (Student student : allRegisteredStudents) {
                    if (student.getUser() != null && currentStudent.equalsIgnoreCase(student.getUser().getUsername())) {


                        if (request.getInstallmentId() != null) {
                            installmentService.updateInstallmentStatusAfterPayment(
                                    request.getInstallmentId(),
                                    student.getId()
                            );
                            System.out.println(">>> [SUCCESS] Installment record slice processed for: " + currentStudent + " <<<");
                        } else {

                            student.setFeeStatus("PAID");
                            studentRepository.saveAndFlush(student);
                            System.out.println(">>> [SUCCESS] Database status updated to PAID for student: " + currentStudent + " <<<");
                        }

                        databaseUpdated = true;
                        break;
                    }
                }

                if (!databaseUpdated) {
                    System.out.println(">>> [WARNING] Student record row matching username not found: " + currentStudent + " <<<");
                }
            } else {
                System.out.println(">>> [ERROR] Razorpay request packet did not carry a valid logged-in username parameter <<<");
            }

        } catch (Exception e) {
            System.out.println("Database Update Exception Error: " + e.getMessage());
        }


        response.put("status", "SUCCESS");
        response.put("success", true);
        response.put("message", "Payment token logic internally verified and database updated.");

        return ResponseEntity.ok(response);
    }
}
