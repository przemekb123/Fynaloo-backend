package com.fynaloo.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDetailsDTO {
    private Long id;
    private String description; // Opis wydatku
    private BigDecimal amount; // Kwota wydatku
    private String paidBy; // Username osoby, która zapłaciła
    private String firstName;
    private String lastName;
    private String currency;

    private Set<ParticipantInfoDTO> participants;

    private Set<PaidDebtInfoDTO> paidDebts;
}
