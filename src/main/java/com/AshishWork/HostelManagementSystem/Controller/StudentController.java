package com.AshishWork.HostelManagementSystem.Controller;


import com.AshishWork.HostelManagementSystem.Dto.ComplaintDTO;
import com.AshishWork.HostelManagementSystem.Dto.RoomDTO;
import com.AshishWork.HostelManagementSystem.Dto.StudentDTO;
import com.AshishWork.HostelManagementSystem.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
//@CrossOrigin(origins = "*")

public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/profile/{username}")
    public ResponseEntity<StudentDTO> getProfile(@PathVariable("username") String username) {
        try {
            return ResponseEntity.ok(studentService.getProfile(username));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/rooms/available")
    public ResponseEntity<List<RoomDTO>> getAvailableRooms() {
        return ResponseEntity.ok(studentService.getAvailableRooms());
    }

    @PostMapping("/complaints/{username}")
    public ResponseEntity<ComplaintDTO> fileComplaint(
            @PathVariable("username") String username,
            @RequestBody ComplaintDTO dto) {

        System.out.println("Complaint API hit for: " + username);

        return ResponseEntity.ok(studentService.fileComplaint(username, dto));
    }

    @GetMapping("/complaints/{username}")
    public ResponseEntity<List<ComplaintDTO>> getMyComplaints(@PathVariable String username) {
        return ResponseEntity.ok(studentService.getMyComplaints(username));
    }


    @PutMapping("/block/{rollNumber}")
    public ResponseEntity<String> blockStudent(@PathVariable("rollNumber") String rollNumber) {
        try {
            studentService.blockStudent(rollNumber);
            return ResponseEntity.ok("Student blocked successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error blocking student: " + e.getMessage());
        }
    }
}