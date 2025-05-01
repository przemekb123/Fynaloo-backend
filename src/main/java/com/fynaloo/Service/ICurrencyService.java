package com.fynaloo.Service;

import java.math.BigDecimal;

public interface ICurrencyService {
    /**
     * Zwraca kurs waluty względem PLN.
     *
     * @param currency waluta (np. EUR, USD, GBP)
     * @return kurs jako BigDecimal (np. 0.23 dla EUR)
     */
    BigDecimal getExchangeRate(String currency);
    void refreshRates();

}
