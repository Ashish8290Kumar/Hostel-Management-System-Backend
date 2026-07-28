
package com.AshishWork.HostelManagementSystem.Impl;

import com.AshishWork.HostelManagementSystem.Dto.ComplaintDTO;
import com.AshishWork.HostelManagementSystem.Dto.RoomDTO;
import com.AshishWork.HostelManagementSystem.Dto.StudentDTO;
import com.AshishWork.HostelManagementSystem.Dto.UserDTO;
import com.AshishWork.HostelManagementSystem.Entity.Complaint;
import com.AshishWork.HostelManagementSystem.Entity.Room;
import com.AshishWork.HostelManagementSystem.Entity.Student;
import com.AshishWork.HostelManagementSystem.Enum.RoomStatus;
import com.AshishWork.HostelManagementSystem.Repositroy.ComplaintRepository;
import com.AshishWork.HostelManagementSystem.Repositroy.RoomRepository;
import com.AshishWork.HostelManagementSystem.Repositroy.StudentRepository;
import com.AshishWork.HostelManagementSystem.Repositroy.InstallmentRepository; // 🚀 Added missing import
import com.AshishWork.HostelManagementSystem.Service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final RoomRepository roomRepository;
    private final ComplaintRepository complaintRepository;


    private final InstallmentRepository installmentRepository;

    @Override
    public StudentDTO getProfile(String username) {
        Student student = studentRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Student Profile Not Found!"));
        return convertToStudentDTO(student);
    }

    private StudentDTO convertToStudentDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setRollNumber(student.getRollNumber());
        dto.setFeeStatus(student.getFeeStatus());
        dto.setRoomNumber(student.getRoom() != null ? student.getRoom().getRoomNumber() : "UNASSIGNED");


        if (installmentRepository != null && student.getId() != null) {
            java.util.List<com.AshishWork.HostelManagementSystem.Entity.Installment> activePlan =
                    installmentRepository.findByStudentIdOrderByInstallmentNumberAsc(student.getId()); // [INDEX]
            dto.setInstallments(activePlan);
        }


        if (student.getRoom() != null) {
            dto.setRoomType(student.getRoom().getRoomType());
            dto.setPricePerMonth(student.getRoom().getPricePerMonth());
            dto.setCapacity(student.getRoom().getCapacity());
        } else {
            dto.setRoomType("SINGLE");
            dto.setPricePerMonth(0.0);
            dto.setCapacity(1);
        }

        UserDTO uDTO = new UserDTO();
        uDTO.setId(student.getUser().getId());
        uDTO.setUsername(student.getUser().getUsername());
        uDTO.setFullName(student.getUser().getFullName());
        uDTO.setEmail(student.getUser().getEmail());
        uDTO.setPhone(student.getUser().getPhone());
        uDTO.setRole(student.getUser().getRole());
        dto.setUserDetails(uDTO);
        return dto;
    }

    @Override
    public List<RoomDTO> getAvailableRooms() {
        return roomRepository.findAll()
                .stream()
                .map(this::convertToRoomDTO)
                .collect(Collectors.toList());
    }

    private RoomDTO convertToRoomDTO(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setId(room.getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setRoomType(room.getRoomType());
        dto.setCapacity(room.getCapacity());
        dto.setCurrentOccupancy(room.getCurrentOccupancy());
        dto.setPricePerMonth(room.getPricePerMonth());
        dto.setStatus(String.valueOf(room.getStatus() != null ? room.getStatus() : RoomStatus.AVAILABLE));
        return dto;
    }

    @Override
    public ComplaintDTO fileComplaint(String username, ComplaintDTO dto) {
        Student student = studentRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Student Not Found!"));

        Complaint complaint = new Complaint();
        complaint.setStudent(student);
        complaint.setTitle(dto.getTitle());
        complaint.setDescription(dto.getDescription());
        complaint.setStatus("Open");
        return convertToComplaintDTO(complaintRepository.save(complaint));
    }

    @Override
    public List<ComplaintDTO> getMyComplaints(String username) {
        return complaintRepository.findByStudentUserUsername(username).stream().map(this::convertToComplaintDTO).collect(Collectors.toList());
    }

    @Override
    public void blockStudent(String rollNumber) {

        List<Student> students = studentRepository.findAll();

        for (Student student : students) {

            if (student.getRollNumber() != null && student.getRollNumber().equalsIgnoreCase(rollNumber)) {


                if (student.getRoom() != null) {
                    Room room = student.getRoom();


                    int currentOccupancy = room.getCurrentOccupancy() != null ? room.getCurrentOccupancy() : 0;
                    if (currentOccupancy > 0) {
                        room.setCurrentOccupancy(currentOccupancy - 1);
                    }


                    if (room.getStatus() != null && "FULL".equalsIgnoreCase(String.valueOf(room.getStatus()))) {
                        room.setStatus(com.AshishWork.HostelManagementSystem.Enum.RoomStatus.AVAILABLE);
                    }


                    roomRepository.saveAndFlush(room);


                    student.setRoom(null);
                }


                student.setFeeStatus("BLOCKED");


                studentRepository.saveAndFlush(student);
                System.out.println(">>> BLOCK MATRIX SUCCESS: Student " + rollNumber + " unlinked from room and blocked successfully <<<");
                return;
            }
        }
        throw new RuntimeException("Student with Roll Number " + rollNumber + " not found!");
    }

    private ComplaintDTO convertToComplaintDTO(Complaint complaint) {
        ComplaintDTO dto = new ComplaintDTO();
        dto.setId(complaint.getId());
        dto.setStudentName(complaint.getStudent().getUser().getUsername());
        dto.setRollNumber(complaint.getStudent().getRollNumber());
        dto.setTitle(complaint.getTitle());
        dto.setDescription(complaint.getDescription());
        dto.setStatus(complaint.getStatus());
        dto.setCreatedAt(complaint.getCreatedAt());
        return dto;
    }
}

