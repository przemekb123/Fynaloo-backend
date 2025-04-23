package com.fynaloo.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaidDebtInfoDTO {
    private Long paidToUserId;      // ID osoby, do której zapłacono
    private String paidToUsername;  // Username osoby, do której zapłacono
    private BigDecimal amountPaid;  // Kwota, którą spłacono
}

