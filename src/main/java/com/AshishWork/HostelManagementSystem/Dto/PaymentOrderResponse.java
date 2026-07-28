package com.AshishWork.HostelManagementSystem.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PaymentOrderResponse {

    private String keyId;
    private String orderId;
    private Integer amount;
    private String currency;
    private String receipt;
}
