package com.fynaloo.Service.Impl;

import com.fynaloo.Dto.AdjustPriceDifferenceRequest;
import com.fynaloo.Dto.ManualDebtDetailsDTO;
import com.fynaloo.Dto.ManualDebtRequest;
import com.fynaloo.Dto.RecalculateDebtRequest;
import com.fynaloo.Mapper.ManualDebtMapper;
import com.fynaloo.Model.Entity.ManualDebt;
import com.fynaloo.Model.Entity.User;
import com.fynaloo.Repository.ManualDebtRepository;
import com.fynaloo.Repository.UserRepository;
import com.fynaloo.Service.IBalanceService;
import com.fynaloo.Service.IManualDebtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ManualDebtServiceImpl implements IManualDebtService {

    private final ManualDebtRepository manualDebtRepository;
    private final ManualDebtMapper manualDebtMapper;
    private final UserRepository userRepository;
    private final IBalanceService balanceService;

    @Override
    @Transactional
    public ManualDebtDetailsDTO addManualDebt(ManualDebtRequest request) {
        User debtor = userRepository.findByUsername(request.getDebtor())
                .orElseThrow(() -> new RuntimeException("Debtor not found"));
        User creditor = userRepository.findByUsername(request.getCreditor())
                .orElseThrow(() -> new RuntimeException("Creditor not found"));

            ManualDebt manualDebt = new ManualDebt();
            manualDebt.setDebtor(debtor);
            manualDebt.setCreditor(creditor);
            manualDebt.setAmount(request.getAmount());
            manualDebt.setDescription(request.getDescription());
            manualDebt.setCreatedAt(LocalDateTime.now());
            manualDebt.setSettled(false);

            manualDebtRepository.save(manualDebt);
            balanceService.ModifyBalance(debtor, creditor, request.getAmount(), request.getCurrency());

        return manualDebtMapper.toManualDebtDetailsDTO(manualDebt);

    }


    @Override
    public List<ManualDebtDetailsDTO> getDebtsForUser(Long userId) {
        List<ManualDebt> debts = manualDebtRepository.findByDebtorId(userId);
        return debts.stream()
                .map(manualDebtMapper::toManualDebtDetailsDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void settleManualDebt(Long debtId) {
        ManualDebt debt = manualDebtRepository.findById(debtId)
                .orElseThrow(() -> new RuntimeException("Debt not found"));
        debt.setSettled(true);
        manualDebtRepository.save(debt);
        balanceService.ModifyBalance(
                debt.getCreditor(),
                debt.getDebtor(),
                debt.getAmount(),
                debt.getCurrency()
        );
    }



    @Override
    public void adjustPriceDifference(AdjustPriceDifferenceRequest request) {
        User payer = userRepository.findById(request.getPayerId())
                .orElseThrow(() -> new RuntimeException("Payer not found"));

        BigDecimal totalAmount = request.getDifferenceAmount();
        int numberOfUsers = request.getAffectedUserIds().size();
        BigDecimal amountPerUser = totalAmount.divide(BigDecimal.valueOf(numberOfUsers), 2, RoundingMode.HALF_UP);

        for (Long userId : request.getAffectedUserIds()) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            ManualDebt debt = new ManualDebt();
            debt.setDebtor(payer);
            debt.setCreditor(user);
            debt.setAmount(amountPerUser);
            debt.setSettled(false);
            debt.setCreatedAt(LocalDateTime.now());
            debt.setDescription("Adjustment due to price difference");

            manualDebtRepository.save(debt);
        }
    }
}