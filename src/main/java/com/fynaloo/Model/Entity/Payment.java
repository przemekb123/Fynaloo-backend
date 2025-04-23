package com.fynaloo.Model.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User payer;

    @ManyToOne
    private User payee;

    @Column(nullable = false)
    private BigDecimal amount;
    private String Currency;

    private LocalDateTime paidAt = LocalDateTime.now();

    private String description;
}
