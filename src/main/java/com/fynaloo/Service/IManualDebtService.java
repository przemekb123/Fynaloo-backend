package com.fynaloo.Service;

import com.fynaloo.Dto.AdjustPriceDifferenceRequest;
import com.fynaloo.Dto.ManualDebtDetailsDTO;
import com.fynaloo.Dto.ManualDebtRequest;
import com.fynaloo.Dto.RecalculateDebtRequest;

import java.util.List;

public interface IManualDebtService {
    ManualDebtDetailsDTO addManualDebt(ManualDebtRequest request);
    List<ManualDebtDetailsDTO> getDebtsForUser(Long userId);
    void settleManualDebt(Long debtId);
    void adjustPriceDifference(AdjustPriceDifferenceRequest request);
}
