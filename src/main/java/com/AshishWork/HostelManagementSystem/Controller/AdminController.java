package com.AshishWork.HostelManagementSystem.Controller;

import com.AshishWork.HostelManagementSystem.Dto.ComplaintDTO;
import com.AshishWork.HostelManagementSystem.Dto.RoomDTO;
import com.AshishWork.HostelManagementSystem.Dto.StudentDTO;
import com.AshishWork.HostelManagementSystem.Service.AdminService;
import com.AshishWork.HostelManagementSystem.Entity.User;
import com.AshishWork.HostelManagementSystem.Repositroy.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")

public class AdminController {

    @Autowired private AdminService adminService;
    @Autowired private UserRepository userRepository;

    @GetMapping("/students")
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        System.out.println("Admin students controller hit");
        return ResponseEntity.ok(adminService.getAllStudents());
    }

    @PostMapping("/rooms")
    public ResponseEntity<RoomDTO> addRoom(@RequestBody RoomDTO dto) {
        return ResponseEntity.ok(adminService.addRoom(dto));
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<RoomDTO>> getAllRooms() {
        return ResponseEntity.ok(adminService.getAllRooms());
    }

    @PutMapping("/students/{studentId}/assign-room")
    public ResponseEntity<String> assignRoom(@PathVariable("studentId") Long studentId, @RequestParam String roomNumber) {
        try {
            return ResponseEntity.ok(adminService.assignRoom(studentId, roomNumber));
        } catch(RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/complaints")
    public ResponseEntity<List<ComplaintDTO>> getAllComplaints() {
        return ResponseEntity.ok(adminService.getAllComplaints());
    }


    @PutMapping("/complaints/{id}/status")
    public ResponseEntity<ComplaintDTO> updateComplaintStatus(@PathVariable("id") Long id, @RequestParam String status) {
        return ResponseEntity.ok(adminService.updateComplaintStatus(id, status));
    }


    @PutMapping("/users/{userId}/toggle-access")
    public ResponseEntity<String> toggleAccess(@PathVariable Long userId, @RequestParam boolean enabled) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(enabled);
        userRepository.save(user);
        return ResponseEntity.ok("User configuration toggled securely via REST engine grid tracking.");
    }


    @PutMapping("/students/{id}")
    public ResponseEntity<String> updateStudentDetails(@PathVariable("id") Long id, @RequestBody StudentDTO dto) {
        try {

            adminService.updateStudentDetails(id, dto);


            if (dto.getRoomNumber() != null && !dto.getRoomNumber().trim().isEmpty()) {
                String roomNo = "Unassigned".equalsIgnoreCase(dto.getRoomNumber()) ? "" : dto.getRoomNumber();
                adminService.assignRoom(id, roomNo);
            }

            return ResponseEntity.ok("Student details and foreign key relationship locked permanently inside PostgreSQL!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update student transaction: " + e.getMessage());
        }
    }


}
