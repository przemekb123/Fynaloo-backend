package com.fynaloo.Mapper;

import com.fynaloo.Dto.PaymentDetailsDTO;
import com.fynaloo.Model.Entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "payer.username", target = "payerUsername")
    @Mapping(source = "payee.username", target = "payeeUsername")
    PaymentDetailsDTO toPaymentDetailsDTO(Payment payment);
}
