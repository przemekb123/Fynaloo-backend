package com.fynaloo.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdjustPriceDifferenceRequest {
    private Long payerId;
    private BigDecimal differenceAmount; // zmiana ceny
    private Set<Long> affectedUserIds;    // osoby, których dotyczy zmiana
}