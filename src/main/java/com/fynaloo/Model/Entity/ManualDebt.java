package com.fynaloo.Model.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "manual_debts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManualDebt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "debtor_id", nullable = false)
    private User debtor; //dluznik

    @ManyToOne(optional = false)
    @JoinColumn(name = "creditor_id", nullable = false)
    private User creditor; //wierzyciel

    @Column(nullable = false)
    private BigDecimal amount;
    @Column
    private String currency;

    @Column(nullable = false)
    private Boolean settled = false;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private String description;

}
