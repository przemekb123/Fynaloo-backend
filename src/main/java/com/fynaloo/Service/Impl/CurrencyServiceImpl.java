package com.fynaloo.Service.Impl;

import com.fynaloo.Dto.CurrencyApiResponse;
import com.fynaloo.Service.ICurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements ICurrencyService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_CURRENCY = "PLN";
    private static final String API_URL = "https://api.frankfurter.app/latest";

    private Map<String, BigDecimal> currencyCache = new HashMap<>();
    private boolean cacheLoaded = false;

    @Override
    public BigDecimal getExchangeRate(String currency) {
        if (currency.equalsIgnoreCase(BASE_CURRENCY)) {
            return BigDecimal.ONE;
        }

        if (currencyCache.containsKey(currency)) {
            return currencyCache.get(currency);
        }

        if (!cacheLoaded) {
            loadRates();
        }

        return currencyCache.getOrDefault(currency, BigDecimal.ONE);
    }

    @Override
    public void refreshRates() {
        cacheLoaded = false;
        loadRates();
    }

    @Scheduled(cron = "0 0 2 * * *") // Codziennie o 2:00 w nocy
    public void scheduledRefreshRates() {
        System.out.println("Odświeżanie kursów walut...");
        refreshRates();
    }

    private void loadRates() {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(API_URL)
                    .queryParam("base", BASE_CURRENCY)
                    .build()
                    .toUriString();

            CurrencyApiResponse response = restTemplate.getForObject(url, CurrencyApiResponse.class);

            if (response != null && response.getRates() != null) {
                currencyCache.clear();
                currencyCache.putAll(response.getRates());
                cacheLoaded = true;
                System.out.println("Kursy walut załadowane poprawnie.");
            }
        } catch (Exception e) {
            System.err.println("Błąd pobierania kursów walut: " + e.getMessage());
            cacheLoaded = true; // żeby nie próbować w nieskończoność
        }
    }
}
