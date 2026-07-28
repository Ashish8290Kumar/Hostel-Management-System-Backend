package com.AshishWork.HostelManagementSystem.Service;

import com.AshishWork.HostelManagementSystem.Dto.ComplaintDTO;
import com.AshishWork.HostelManagementSystem.Dto.RoomDTO;
import com.AshishWork.HostelManagementSystem.Dto.StudentDTO;

import java.util.List;

public interface StudentService {
    StudentDTO getProfile(String username);
    List<RoomDTO> getAvailableRooms();

    ComplaintDTO fileComplaint(String username, ComplaintDTO dto);
    List<ComplaintDTO> getMyComplaints(String username);

    void blockStudent(String rollNumber);
}
