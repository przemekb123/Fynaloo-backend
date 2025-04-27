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

    @Override
    @Transactional
    public ManualDebtDetailsDTO addManualDebt(ManualDebtRequest request) {
        User debtor = userRepository.findByUsername(request.getDebtor())
                .orElseThrow(() -> new RuntimeException("Debtor not found"));
        User creditor = userRepository.findByUsername(request.getCreditor())
                .orElseThrow(() -> new RuntimeException("Creditor not found"));

        // Szukamy istniejącego długu pomiędzy użytkownikami
        Optional<ManualDebt> existingDebtOpt = manualDebtRepository.findDebtBetween(debtor, creditor);

        if (existingDebtOpt.isPresent()) {
            ManualDebt existingDebt = existingDebtOpt.get();
            BigDecimal newAmount = existingDebt.getAmount().add(request.getAmount());

            if (newAmount.compareTo(BigDecimal.ZERO) == 0) {
                // Rozliczenie długu
                existingDebt.setAmount(BigDecimal.ZERO);
                existingDebt.setSettled(true);
            } else {
                existingDebt.setAmount(newAmount);
            }

            manualDebtRepository.save(existingDebt);
            return manualDebtMapper.toManualDebtDetailsDTO(existingDebt);

        } else {
            ManualDebt manualDebt = new ManualDebt();
            manualDebt.setDebtor(debtor);
            manualDebt.setCreditor(creditor);
            manualDebt.setAmount(request.getAmount());
            manualDebt.setDescription(request.getDescription());
            manualDebt.setCreatedAt(LocalDateTime.now());
            manualDebt.setSettled(false);

            manualDebtRepository.save(manualDebt);
            return manualDebtMapper.toManualDebtDetailsDTO(manualDebt);
        }
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
    }

    @Override
    public void recalculateDebt(RecalculateDebtRequest request) {
        Long debtorId = request.getDebtorId();
        Long creditorId = request.getCreditorId();
        BigDecimal newAmount = request.getAmount();

        // Szukamy istniejącego długu w obie strony
        List<ManualDebt> debtorDebts = manualDebtRepository.findByDebtorIdAndCreditorId(debtorId, creditorId);
        List<ManualDebt> creditorDebts = manualDebtRepository.findByDebtorIdAndCreditorId(creditorId, debtorId);

        BigDecimal debtorTotal = debtorDebts.stream()
                .filter(debt -> !debt.getSettled())
                .map(ManualDebt::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal creditorTotal = creditorDebts.stream()
                .filter(debt -> !debt.getSettled())
                .map(ManualDebt::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal result = debtorTotal.subtract(creditorTotal).add(newAmount);

        // Usuwamy stare długi (lub oznaczamy jako spłacone)
        debtorDebts.forEach(debt -> {
            debt.setSettled(true);
            manualDebtRepository.save(debt);
        });
        creditorDebts.forEach(debt -> {
            debt.setSettled(true);
            manualDebtRepository.save(debt);
        });

        if (result.compareTo(BigDecimal.ZERO) > 0) {
            // Debtor nadal jest winny creditorowi
            ManualDebt newDebt = new ManualDebt();
            newDebt.setDebtor(userRepository.findById(debtorId).orElseThrow(() -> new RuntimeException("Debtor not found")));
            newDebt.setCreditor(userRepository.findById(creditorId).orElseThrow(() -> new RuntimeException("Creditor not found")));
            newDebt.setAmount(result);
            newDebt.setCreatedAt(LocalDateTime.now());
            newDebt.setSettled(false);
            manualDebtRepository.save(newDebt);
        } else if (result.compareTo(BigDecimal.ZERO) < 0) {
            // Creditor jest teraz winny debtorowi
            ManualDebt newDebt = new ManualDebt();
            newDebt.setDebtor(userRepository.findById(creditorId).orElseThrow(() -> new RuntimeException("Creditor not found")));
            newDebt.setCreditor(userRepository.findById(debtorId).orElseThrow(() -> new RuntimeException("Debtor not found")));
            newDebt.setAmount(result.abs());
            newDebt.setCreatedAt(LocalDateTime.now());
            newDebt.setSettled(false);
            manualDebtRepository.save(newDebt);
        }
        // Jeśli zero - nie tworzymy nowego długu

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
