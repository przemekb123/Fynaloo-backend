package com.fynaloo.Repository;

import com.fynaloo.Model.Entity.ExpenseParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpenseParticipantRepository extends JpaRepository<ExpenseParticipant, Long> {
    Optional<ExpenseParticipant> findByExpenseIdAndUserId(Long expenseId, Long userId);
}