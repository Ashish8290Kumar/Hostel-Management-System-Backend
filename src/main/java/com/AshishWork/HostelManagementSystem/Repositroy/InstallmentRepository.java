package com.AshishWork.HostelManagementSystem.Repositroy;

import com.AshishWork.HostelManagementSystem.Entity.Installment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstallmentRepository extends JpaRepository<Installment, Long> {
    List<Installment> findByStudentIdOrderByInstallmentNumberAsc(Long studentId);

}
