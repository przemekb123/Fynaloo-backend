package com.fynaloo.Controller;

import com.fynaloo.Dto.BalanceSummaryDTO;
import com.fynaloo.Service.IBalanceService;
import com.fynaloo.Service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/balances")
@RequiredArgsConstructor
public class BalanceController {

    private final IBalanceService balanceService;
    private final IUserService userService;

    @GetMapping("/me")
    public List<BalanceSummaryDTO> getMyBalances() {
        Long currentUserId = userService.getCurrentUser().getId();
        return balanceService.getBalancesForUser(currentUserId);
    }
}
