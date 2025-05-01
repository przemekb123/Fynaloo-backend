package com.fynaloo.Service;

import com.fynaloo.Dto.BalanceSummaryDTO;
import com.fynaloo.Model.Entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface IBalanceService {

    void ModifyBalance(User user1, User user2, BigDecimal amount, String currency);
    List<BalanceSummaryDTO> getBalancesForUser(Long userId);

}
