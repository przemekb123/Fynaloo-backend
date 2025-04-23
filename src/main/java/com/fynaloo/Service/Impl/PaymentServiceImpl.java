package com.fynaloo.Service.Impl;

import com.fynaloo.Dto.PaymentDetailsDTO;
import com.fynaloo.Mapper.PaymentMapper;
import com.fynaloo.Model.Entity.Payment;
import com.fynaloo.Model.Entity.User;
import com.fynaloo.Repository.PaymentRepository;
import com.fynaloo.Repository.UserRepository;
import com.fynaloo.Service.IPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements IPaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final UserRepository userRepository;


    @Override
    public PaymentDetailsDTO recordPayment(PaymentDetailsDTO paymentDTO) {
        // Stworzenie nowego obiektu Payment
        Payment payment = new Payment();
        payment.setAmount(paymentDTO.getAmount());
        payment.setDescription(paymentDTO.getDescription());
        payment.setCurrency(paymentDTO.getCurrency());

        //  Wyszukanie `użytkownika płacącego i odbiorcy
        User payer = userRepository.findByUsername(paymentDTO.getPayerUsername())
                .orElseThrow(() -> new RuntimeException("Payer not found"));

        User payee = userRepository.findByUsername(paymentDTO.getPayeeUsername())
                .orElseThrow(() -> new RuntimeException("Payee not found"));

        payment.setPayer(payer);
        payment.setPayee(payee);

        // Zapisanie płatności
        Payment savedPayment = paymentRepository.save(payment);

        //  Mapowanie na DTO
        return paymentMapper.toPaymentDetailsDTO(savedPayment);
    }

    @Override
    public List<PaymentDetailsDTO> getPaymentsForUser(Long userId) {
        List<Payment> payments = paymentRepository.findByPayerId(userId);
        return payments.stream()
                .map(paymentMapper::toPaymentDetailsDTO)
                .collect(Collectors.toList());
    }
}
