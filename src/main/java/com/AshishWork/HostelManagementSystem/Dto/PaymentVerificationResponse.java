package com.AshishWork.HostelManagementSystem.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PaymentVerificationResponse {
    private boolean success;
    private String message;
    private String orderId;
    private String paymentId;
}
