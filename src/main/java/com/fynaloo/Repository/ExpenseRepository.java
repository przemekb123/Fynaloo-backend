package com.fynaloo.Repository;

import com.fynaloo.Model.Entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findAllByPaidById(Long userId);

    @Query("SELECT DISTINCT e FROM Expense e LEFT JOIN e.participants p WHERE e.paidBy.id = :userId OR p.user.id = :userId")
    List<Expense> findAllRelatedToUser(@Param("userId") Long userId);
}