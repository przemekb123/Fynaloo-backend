package com.fynaloo.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecalculateDebtRequest {
    private Long debtorId;
    private Long creditorId;
    private BigDecimal amount;
}