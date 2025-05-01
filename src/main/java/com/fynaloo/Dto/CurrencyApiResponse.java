package com.fynaloo.Dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class CurrencyApiResponse {

        private Map<String, BigDecimal> rates;


}
