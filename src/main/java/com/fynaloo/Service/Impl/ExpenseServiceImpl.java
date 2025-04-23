package com.fynaloo.Service.Impl;

import com.fynaloo.Dto.ExpenseDetailsDTO;
import com.fynaloo.Dto.ExpenseRequest;
import com.fynaloo.Dto.ParticipantInfoDTO;
import com.fynaloo.Mapper.ExpenseMapper;
import com.fynaloo.Model.Entity.Expense;
import com.fynaloo.Model.Entity.ExpenseParticipant;
import com.fynaloo.Model.Entity.Group;
import com.fynaloo.Model.Entity.User;
import com.fynaloo.Repository.ExpenseParticipantRepository;
import com.fynaloo.Repository.ExpenseRepository;
import com.fynaloo.Repository.GroupRepository;
import com.fynaloo.Repository.UserRepository;
import com.fynaloo.Service.IExpenseService;
import com.fynaloo.Service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpenseServiceImpl implements IExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseParticipantRepository expenseParticipantRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final ExpenseMapper expenseMapper;
    private final IUserService userService;
    @Override
    public ExpenseDetailsDTO createExpense(ExpenseRequest request) {
        // 1. Stworzenie nowego obiektu Expense
        Expense expense = new Expense();
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setCurrency(request.getCurrency());

        // Wyszukanie użytkownika, który zapłacił
        User payer = userRepository.findByUsername(request.getPaidBy())
                .orElseThrow(() -> new RuntimeException("Payer not found"));
        expense.setPaidBy(payer);

        // Przypisanie wydatku do grupy
        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found"));
        expense.setGroup(group);

        // Zapisanie wydatku
        expense = expenseRepository.save(expense);

        // 2. Dodanie uczestników wydatku
        for (ParticipantInfoDTO participantDTO : request.getParticipants()) {
            User participantUser = userRepository.findById(participantDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("Participant user not found"));

            ExpenseParticipant participant = new ExpenseParticipant();
            participant.setExpense(expense);
            participant.setUser(participantUser);
            participant.setShareAmount(participantDTO.getShareAmount());
            participant.setSettled(false);

            expenseParticipantRepository.save(participant);
        }

        // 3. Mapowanie z powrotem na DTO
        return expenseMapper.toExpenseDetailsDTO(expense);
    }

        @Override
        public ExpenseDetailsDTO getExpenseDetails (Long expenseId){
            Expense expense = expenseRepository.findById(expenseId)
                    .orElseThrow(() -> new RuntimeException("Expense not found"));
            return expenseMapper.toExpenseDetailsDTO(expense);
        }

    @Override
    public List<ExpenseDetailsDTO> getExpensesForUser(Long userId) {
        List<Expense> expenses = expenseRepository.findAllRelatedToUser(userId);
        return expenses.stream()
                .map(expenseMapper::toExpenseDetailsDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void settleParticipant(Long expenseId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        User currentUser = userService.getCurrentUser();

        ExpenseParticipant participant = expenseParticipantRepository
                .findByExpenseIdAndUserId(expenseId, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("You are not a participant in this expense"));

        if (participant.isSettled()) {
            throw new RuntimeException("Expense already settled");
        }

        participant.setSettled(true);
        expenseParticipantRepository.save(participant);
    }
}
