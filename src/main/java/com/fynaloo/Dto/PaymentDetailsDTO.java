package com.fynaloo.Dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetailsDTO {
    private Long id;               // ID płatności
    private String payerUsername;  // Username płacącego
    private String payeeUsername;  // Username odbiorcy
    private BigDecimal amount;     // Kwota płatności
    private String currency;       // Waluta
    private LocalDateTime paidAt;  // Data i godzina wykonania płatności
    private String description;    // Opis płatności
}
