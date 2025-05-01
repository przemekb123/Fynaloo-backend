package com.fynaloo.Dto;

import com.fynaloo.Model.Entity.User;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManualDebtRequest {
    private String debtor;
    private String creditor;
    private BigDecimal amount;
    private String currency;

    private String description;

}
