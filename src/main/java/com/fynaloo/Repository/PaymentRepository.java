package com.fynaloo.Repository;

import com.fynaloo.Model.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByPayerId(Long userId);
}