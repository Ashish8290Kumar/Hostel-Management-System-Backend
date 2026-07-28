package com.AshishWork.HostelManagementSystem.Dto;


import lombok.Data;

@Data
public class PaymentOrderRequest {
    private String username;
    private Double amount;
    private String purpose;
}
