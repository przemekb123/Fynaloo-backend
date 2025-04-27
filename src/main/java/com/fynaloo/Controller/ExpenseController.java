package com.fynaloo.Controller;

import com.fynaloo.Dto.ApiResponse;
import com.fynaloo.Dto.ExpenseDetailsDTO;
import com.fynaloo.Dto.ExpenseRequest;
import com.fynaloo.Service.IExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final IExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseDetailsDTO> createExpense(@RequestBody ExpenseRequest request) {
        ExpenseDetailsDTO createdExpense = expenseService.createExpense(request);
        return ResponseEntity.ok(createdExpense);
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<ExpenseDetailsDTO> getExpenseDetails(@PathVariable Long expenseId) {
        ExpenseDetailsDTO expense = expenseService.getExpenseDetails(expenseId);
        return ResponseEntity.ok(expense);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ExpenseDetailsDTO>> getExpensesForUser(@PathVariable Long userId) {
        List<ExpenseDetailsDTO> expenses = expenseService.getExpensesForUser(userId);
        return ResponseEntity.ok(expenses);
    }

    @PostMapping("/{expenseId}/settle")
    public ResponseEntity<ApiResponse> settleExpense(@PathVariable Long expenseId) {
        expenseService.settleParticipant(expenseId);
        return ResponseEntity.ok(new ApiResponse("Debt marked as settled"));
    }
}
