package com.fynaloo.Dto;

import lombok.Data;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserFinancialSummaryDTO {
    private Set<ExpenseDetailsDTO> expenses;
    private Set<ManualDebtDetailsDTO> manualDebts;
}
