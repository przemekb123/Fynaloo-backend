package com.fynaloo.Service;

import com.fynaloo.Dto.ExpenseDetailsDTO;
import com.fynaloo.Dto.ExpenseRequest;

import java.util.List;

public interface IExpenseService {
    ExpenseDetailsDTO createExpense(ExpenseRequest request);
    ExpenseDetailsDTO getExpenseDetails(Long expenseId);

    List<ExpenseDetailsDTO> getExpensesForUser(Long userId);
    void settleParticipant(Long expenseId);
}
