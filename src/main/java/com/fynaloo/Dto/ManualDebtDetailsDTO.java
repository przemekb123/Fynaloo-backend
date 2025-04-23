package com.fynaloo.Dto;

import com.fynaloo.Model.Entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManualDebtDetailsDTO {
    private Long id;
    private String debtor;
    private String creditor;
    private BigDecimal amount;
    private Boolean settled;
    private LocalDateTime createdAt;
    private String description;
}
