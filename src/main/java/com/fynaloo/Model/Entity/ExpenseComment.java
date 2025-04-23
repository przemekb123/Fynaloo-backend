package com.fynaloo.Model.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "expense_comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Expense expense;

    @ManyToOne
    private User author;

    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();
}
