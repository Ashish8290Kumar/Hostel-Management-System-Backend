package com.AshishWork.HostelManagementSystem.Impl;

import com.AshishWork.HostelManagementSystem.Entity.Installment;
import com.AshishWork.HostelManagementSystem.Entity.Student;
import com.AshishWork.HostelManagementSystem.Repositroy.InstallmentRepository;
import com.AshishWork.HostelManagementSystem.Repositroy.StudentRepository;
import com.AshishWork.HostelManagementSystem.Service.InstallmentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class InstallmentServiceImpl implements InstallmentService {

    @Autowired
    private InstallmentRepository installmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    @Transactional
    public void createCustomInstallments(Long studentId, List<Map<String, Object>> installmentList) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student record row not found with ID: " + studentId));


        List<Installment> oldInstallments = installmentRepository.findByStudentIdOrderByInstallmentNumberAsc(studentId);
        installmentRepository.deleteAll(oldInstallments);


        List<Installment> freshSlots = new ArrayList<>();
        for (int i = 0; i < installmentList.size(); i++) {
            Map<String, Object> dataNode = installmentList.get(i);
            Long amt = Long.parseLong(dataNode.get("amount").toString());

            Installment singleSlot = Installment.builder()
                    .student(student)
                    .installmentNumber(i + 1)
                    .amount(amt)
                    .status("PENDING")
                    .build();
            freshSlots.add(singleSlot);
        }

        installmentRepository.saveAll(freshSlots);


        student.setFeeStatus("PENDING");
        studentRepository.saveAndFlush(student);
    }

    @Override
    public List<Installment> getInstallmentsByStudentId(Long studentId) {
        return installmentRepository.findByStudentIdOrderByInstallmentNumberAsc(studentId);
    }

    @Override
    @Transactional
    public Installment updateInstallmentStatusAfterPayment(Long installmentId, Long studentId) {
        Installment target = installmentRepository.findById(installmentId)
                .orElseThrow(() -> new RuntimeException("Target installment allocation index missing: " + installmentId));

        if (!target.getStudent().getId().equals(studentId)) {
            throw new RuntimeException("Security violation mismatch: Installment record does not belong to requested student ID.");
        }


        target.setStatus("PAID");
        target.setPaymentDate(LocalDateTime.now());
        installmentRepository.saveAndFlush(target);


        List<Installment> totalSlots = installmentRepository.findByStudentIdOrderByInstallmentNumberAsc(studentId);
        boolean isEverythingSettled = totalSlots.stream().allMatch(inst -> "PAID".equalsIgnoreCase(inst.getStatus()));

        Student student = studentRepository.findById(studentId).orElse(null);
        if (student != null) {
            if (isEverythingSettled) {
                student.setFeeStatus("PAID");
            } else {
                student.setFeeStatus("PARTIALLY_PAID");
            }
            studentRepository.saveAndFlush(student);
        }

        return target;
    }
}