package com.fynaloo.Service.Impl;

import com.fynaloo.Dto.BalanceSummaryDTO;
import com.fynaloo.Mapper.BalanceMapper;
import com.fynaloo.Model.Entity.Balance;
import com.fynaloo.Model.Entity.User;
import com.fynaloo.Repository.BalanceRepository;
import com.fynaloo.Repository.UserRepository;
import com.fynaloo.Service.IBalanceService;
import com.fynaloo.Service.ICurrencyService;
import com.fynaloo.Service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BalanceServiceImpl implements IBalanceService {

    private final ICurrencyService currencyService;
    private final BalanceRepository balanceRepository;
    private final BalanceMapper balanceMapper;
    private final UserRepository userRepository;

    @Override
    public void ModifyBalance(User user1, User user2, BigDecimal amount, String currency) {
        if(user1.getId().equals(user2.getId())) return;

        BigDecimal rate = "PLN".equalsIgnoreCase(currency)
                ? BigDecimal.ONE : currencyService.getExchangeRate(currency);
        BigDecimal amountPln = amount.multiply(rate);

        // standaryzacja pary użytkowników
        boolean reversed = false;
        if (user1.getId() > user2.getId()) {
            User tmp = user1;
            user1 = user2;
            user2 = tmp;
            amountPln = amountPln.negate();
            reversed = true;
        }

        Balance balance = balanceRepository.findByUsers(user1, user2)
                .orElse(Balance.builder()
                        .user1(user1)
                        .counterparty(user2)
                        .balance(BigDecimal.ZERO)
                        .currency("PLN")
                        .build());

        balance.setBalance(balance.getBalance().add(amountPln));
        balance.setUpdatedAt(java.time.LocalDateTime.now());

        balanceRepository.save(balance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BalanceSummaryDTO> getBalancesForUser(Long userId) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Balance> balances = balanceRepository.findAllByUser(currentUser);

        return balances.stream()
                .map(b -> balanceMapper.toBalanceSummaryDTO(b, currentUser))
                .toList();
    }
}
