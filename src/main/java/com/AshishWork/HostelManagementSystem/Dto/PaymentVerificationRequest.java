package com.AshishWork.HostelManagementSystem.Dto;

import lombok.Data;

@Data
public class PaymentVerificationRequest {
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
    private String username;
    private Long installmentId;
}
