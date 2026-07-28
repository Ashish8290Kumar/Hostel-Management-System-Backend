package com.AshishWork.HostelManagementSystem.Service;

import com.AshishWork.HostelManagementSystem.Dto.ComplaintDTO;
import com.AshishWork.HostelManagementSystem.Dto.RoomDTO;
import com.AshishWork.HostelManagementSystem.Dto.StudentDTO;

import java.util.List;

public interface AdminService {
    List<StudentDTO> getAllStudents();
    RoomDTO addRoom(RoomDTO dto);


    List<RoomDTO> getAllRooms();
    String assignRoom(Long studentId, String roomNumber);


    List<ComplaintDTO> getAllComplaints();
    ComplaintDTO updateComplaintStatus(Long id, String status);

    void updateStudentDetails(Long id, StudentDTO dto);

}
