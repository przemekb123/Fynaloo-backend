package com.fynaloo.Dto;

import com.fynaloo.Model.Entity.ExpenseParticipant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseRequest {
    private Long groupId;
    private String paidBy;
    private BigDecimal amount;
    private String currency;
    private String description;
    private Set<ParticipantInfoDTO> participants;
}
