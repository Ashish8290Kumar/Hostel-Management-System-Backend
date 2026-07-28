package com.AshishWork.HostelManagementSystem.Service;

import com.AshishWork.HostelManagementSystem.Entity.Installment;

import java.util.List;
import java.util.Map;

public interface InstallmentService {
    void createCustomInstallments(Long studentId, List<Map<String, Object>> installmentList);
    List<Installment> getInstallmentsByStudentId(Long studentId);
    Installment updateInstallmentStatusAfterPayment(Long installmentId, Long studentId);
}
