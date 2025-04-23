package com.fynaloo.Controller;

import com.fynaloo.Dto.PaymentDetailsDTO;
import com.fynaloo.Service.IPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final IPaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentDetailsDTO> recordPayment(@RequestBody PaymentDetailsDTO paymentDetailsDTO) {
        PaymentDetailsDTO savedPayment = paymentService.recordPayment(paymentDetailsDTO);
        return ResponseEntity.ok(savedPayment);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentDetailsDTO>> getPaymentsForUser(@PathVariable Long userId) {
        List<PaymentDetailsDTO> payments = paymentService.getPaymentsForUser(userId);
        return ResponseEntity.ok(payments);
    }


}
