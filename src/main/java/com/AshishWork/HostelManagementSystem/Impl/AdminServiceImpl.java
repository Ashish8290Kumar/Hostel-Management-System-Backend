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
import com.AshishWork.HostelManagementSystem.Service.AdminService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.AshishWork.HostelManagementSystem.Enum.RoomStatus.AVAILABLE;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final StudentRepository studentRepository;
    private final ComplaintRepository complaintRepository;
    private final RoomRepository roomRepository;


    @Override
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream().map(this::convertToStudentDTO).collect(Collectors.toList());
    }

    private StudentDTO convertToStudentDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setRollNumber(student.getRollNumber());
        dto.setFeeStatus(student.getFeeStatus());
        dto.setRoomNumber(student.getRoom() != null ? student.getRoom().getRoomNumber() : "UNASSIGNED");

        UserDTO uDto = new UserDTO();
        uDto.setId(student.getUser().getId());
        uDto.setUsername(student.getUser().getUsername());
        uDto.setFullName(student.getUser().getFullName());
        uDto.setEmail(student.getUser().getEmail());
        uDto.setPhone(student.getUser().getPhone());
        uDto.setRole(student.getUser().getRole());
        dto.setUserDetails(uDto);
        dto.setInstallments(student.getInstallments());
        return dto;
    }

    @Override
    public RoomDTO addRoom(RoomDTO dto) {
        Room room = new Room();
        room.setRoomNumber(dto.getRoomNumber());
        room.setRoomType(dto.getRoomType());
        room.setCapacity(dto.getCapacity());
        room.setPricePerMonth(dto.getPricePerMonth());
        room.setStatus(AVAILABLE);   // enum instead of string
        return convertToRoomDTO(roomRepository.save(room));
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
    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll().stream().map(this::convertToRoomDTO).collect(Collectors.toList());
    }

    @Transactional
    public String assignRoom(Long studentId, String roomNumber) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student record drop"));


        if (student.getRoom() != null) {
            Room oldRoom = student.getRoom();
            int currentOcc = oldRoom.getCurrentOccupancy() != null ? oldRoom.getCurrentOccupancy() : 0;
            if (currentOcc > 0) {
                oldRoom.setCurrentOccupancy(currentOcc - 1);
            }
            roomRepository.saveAndFlush(oldRoom);
            student.setRoom(null);
        }


        if (roomNumber != null && !roomNumber.trim().isEmpty()) {
            Room newRoom = roomRepository.findByRoomNumber(roomNumber)
                    .orElseThrow(() -> new RuntimeException("Target Room not initialized"));


            int filled = newRoom.getCurrentOccupancy() != null ? newRoom.getCurrentOccupancy() : 0;
            if (filled >= newRoom.getCapacity()) {
                throw new RuntimeException("Target allocation block is already FULL!");
            }

            newRoom.setCurrentOccupancy(filled + 1);
            roomRepository.saveAndFlush(newRoom);


            student.setRoom(newRoom);
        }


        studentRepository.saveAndFlush(student);
        return "Room alignment index updated safely.";
    }




    @Override
    public List<ComplaintDTO> getAllComplaints() {
        return complaintRepository.findAll().stream().map(this::convertToComplaintDTO).collect(Collectors.toList());
    }

    @Override
    public ComplaintDTO updateComplaintStatus(Long id, String status) {
        Complaint complaint = complaintRepository.findById(id).orElseThrow(() -> new RuntimeException("Complaint not found"));
        complaint.setStatus(status);
        return convertToComplaintDTO(complaintRepository.save(complaint));
    }

    private ComplaintDTO convertToComplaintDTO(Complaint complaint) {
        ComplaintDTO dto = new ComplaintDTO();
        dto.setId(complaint.getId());
        dto.setStudentName(complaint.getStudent().getUser().getFullName());
        dto.setRollNumber(complaint.getStudent().getRollNumber());
        dto.setTitle(complaint.getTitle());
        dto.setDescription(complaint.getDescription());
        dto.setStatus(complaint.getStatus());
        dto.setCreatedAt(complaint.getCreatedAt());
        return dto;
    }



    @Override
    public void updateStudentDetails(Long id, StudentDTO dto) {
        // 1. Database se student profile fetch karenge
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student profile not found with ID: " + id));


        if (student.getUser() != null && dto.getUserDetails() != null) {
            com.AshishWork.HostelManagementSystem.Entity.User user = student.getUser();
            user.setFullName(dto.getUserDetails().getFullName());
            user.setEmail(dto.getUserDetails().getEmail());
        }


        String targetRoomNumber = dto.getRoomNumber();


        if (targetRoomNumber != null && !targetRoomNumber.isEmpty()) {


            String currentRoomNum = (student.getRoom() != null) ? student.getRoom().getRoomNumber() : "UNASSIGNED";

            if (!currentRoomNum.equalsIgnoreCase(targetRoomNumber)) {


                if (targetRoomNumber.equalsIgnoreCase("Unassigned") || targetRoomNumber.equalsIgnoreCase("UNASSIGNED")) {
                    if (student.getRoom() != null) {
                        Room oldRoom = student.getRoom();
                        if (oldRoom.getCurrentOccupancy() > 0) {
                            oldRoom.setCurrentOccupancy(oldRoom.getCurrentOccupancy() - 1);
                        }
                        oldRoom.setStatus(RoomStatus.AVAILABLE);
                        roomRepository.save(oldRoom);
                        student.setRoom(null);
                    }
                }

                else {
                    Room targetRoom = roomRepository.findByRoomNumber(targetRoomNumber)
                            .orElseThrow(() -> new RuntimeException("Target Room not found: " + targetRoomNumber));

                    if (targetRoom.getCurrentOccupancy() >= targetRoom.getCapacity()) {
                        throw new RuntimeException("Target room " + targetRoomNumber + " is already full!");
                    }


                    if (student.getRoom() != null) {
                        Room oldRoom = student.getRoom();
                        if (oldRoom.getCurrentOccupancy() > 0) {
                            oldRoom.setCurrentOccupancy(oldRoom.getCurrentOccupancy() - 1);
                        }
                        oldRoom.setStatus(RoomStatus.AVAILABLE);
                        roomRepository.save(oldRoom);
                    }


                    student.setRoom(targetRoom);
                    targetRoom.setCurrentOccupancy(targetRoom.getCurrentOccupancy() + 1);

                    if (targetRoom.getCurrentOccupancy().equals(targetRoom.getCapacity())) {
                        targetRoom.setStatus(RoomStatus.FULL);
                    } else {
                        targetRoom.setStatus(RoomStatus.AVAILABLE);
                    }
                    roomRepository.save(targetRoom);
                }
            }
        }


        studentRepository.save(student);
    }



}
