package com.fynaloo.Service;

import com.fynaloo.Dto.PaymentDetailsDTO;

import java.util.List;

public interface IPaymentService {
    PaymentDetailsDTO recordPayment(PaymentDetailsDTO paymentDTO);
    List<PaymentDetailsDTO> getPaymentsForUser(Long userId);
}
