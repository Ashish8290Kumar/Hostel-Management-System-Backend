package com.AshishWork.HostelManagementSystem.Service;

import com.AshishWork.HostelManagementSystem.Dto.PaymentOrderRequest;
import com.AshishWork.HostelManagementSystem.Dto.PaymentOrderResponse;
import com.AshishWork.HostelManagementSystem.Dto.PaymentVerificationRequest;
import com.AshishWork.HostelManagementSystem.Dto.PaymentVerificationResponse;

public interface PaymentService {

    PaymentOrderResponse createOrder(PaymentOrderRequest request);

    PaymentVerificationResponse verifyPayment(PaymentVerificationRequest request);
}
