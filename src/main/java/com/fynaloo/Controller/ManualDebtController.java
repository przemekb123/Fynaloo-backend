package com.fynaloo.Controller;

import com.fynaloo.Dto.*;
import com.fynaloo.Service.IManualDebtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manual-debts")
@RequiredArgsConstructor
public class ManualDebtController {

    private final IManualDebtService manualDebtService;

    @PostMapping
    public ResponseEntity<ManualDebtDetailsDTO> addManualDebt(@RequestBody ManualDebtRequest request) {
        ManualDebtDetailsDTO savedDebt = manualDebtService.addManualDebt(request);
        return ResponseEntity.ok(savedDebt);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ManualDebtDetailsDTO>> getDebtsForUser(@PathVariable Long userId) {
        List<ManualDebtDetailsDTO> debts = manualDebtService.getDebtsForUser(userId);
        return ResponseEntity.ok(debts);
    }

    @PostMapping("/{debtId}/settle")
    public ResponseEntity<String> settleManualDebt(@PathVariable Long debtId) {
        manualDebtService.settleManualDebt(debtId);
        return ResponseEntity.ok("Debt settled successfully");
    }

    @PostMapping("/recalculate")
    public ResponseEntity<ApiResponse> recalculateDebt(@RequestBody RecalculateDebtRequest request) {
        manualDebtService.recalculateDebt(request);

        return ResponseEntity.ok(new ApiResponse("Debt recalculated successfully"));
    }

    //adjust price
    @PostMapping("/adjust-price-difference")
    public ResponseEntity<String> adjustPriceDifference(@RequestBody AdjustPriceDifferenceRequest request) {
        manualDebtService.adjustPriceDifference(request);
        return ResponseEntity.ok("Price difference distributed successfully");
    }
}
