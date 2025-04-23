package com.fynaloo.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantInfoDTO {
    private Long userId;
    private String username;
    private BigDecimal shareAmount; // Ile dany użytkownik ma zapłacić
    private boolean settled; // Czy już spłacił
}
