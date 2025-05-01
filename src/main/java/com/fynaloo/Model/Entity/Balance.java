package com.fynaloo.Model.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "balances", uniqueConstraints = @UniqueConstraint(columnNames = {"user1_id", "user2_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Balance {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;


    @ManyToOne(optional = false)
    @JoinColumn(name = "counterparty_id", nullable = false)
    private User counterparty;



    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
