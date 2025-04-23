package com.fynaloo.Dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private Long payerId;      // ID użytkownika płacącego
    private Long payeeId;      // ID użytkownika odbierającego płatność
    private BigDecimal amount; // Kwota płatności
    private String currency;   // Waluta
    private String description; // Opcjonalny opis
}