package com.fynaloo.Model.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "paid_by", nullable = false)
    private User paidBy;

    @Column(nullable = false)
    private BigDecimal amount;

    private String description;

    @Column(nullable = false)
    private String currency = "PLN";

    @OneToMany(mappedBy = "expense")
    private Set<ExpenseParticipant> participants;

//    @OneToMany(mappedBy = "expense")
//    private List<ExpenseComment> comments;
}
