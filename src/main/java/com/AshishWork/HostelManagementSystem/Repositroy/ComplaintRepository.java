package com.AshishWork.HostelManagementSystem.Repositroy;

import com.AshishWork.HostelManagementSystem.Entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByStudentUserUsername(String username);
}
