package com.fynaloo.Mapper;

import com.fynaloo.Dto.PaymentDetailsDTO;
import com.fynaloo.Model.Entity.Payment;
import com.fynaloo.Model.Entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentMapperTest {

    private PaymentMapper paymentMapper;

    @BeforeEach
    void setUp() {
        paymentMapper = new PaymentMapperImpl();
    }



    @Test
    void should_map_payment_to_payment_details_dto() {
        // given
        User payer = new User();
        payer.setId(1L);
        payer.setUsername("payer");

        User payee = new User();
        payee.setId(2L);
        payee.setUsername("payee");

        Payment payment = new Payment();
        payment.setId(123L);
        payment.setPayer(payer);
        payment.setPayee(payee);
        payment.setAmount(new BigDecimal("300.00"));
        payment.setPaidAt(LocalDateTime.now());

        // when
        PaymentDetailsDTO dto = paymentMapper.toPaymentDetailsDTO(payment);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getPayerUsername()).isEqualTo("payer");
        assertThat(dto.getPayeeUsername()).isEqualTo("payee");
        assertThat(dto.getAmount()).isEqualTo(new BigDecimal("300.00"));
    }
}
