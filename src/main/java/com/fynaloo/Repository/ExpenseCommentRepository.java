package com.fynaloo.Repository;

import com.fynaloo.Model.Entity.ExpenseComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseCommentRepository extends JpaRepository<ExpenseComment, Long> {
}