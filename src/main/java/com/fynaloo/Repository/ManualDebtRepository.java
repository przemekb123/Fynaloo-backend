package com.fynaloo.Repository;

import com.fynaloo.Model.Entity.ManualDebt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManualDebtRepository extends JpaRepository<ManualDebt, Long> {
    List<ManualDebt> findByDebtorId(Long userId);

    List<ManualDebt> findByDebtorIdAndCreditorId(Long debtorId, Long creditorId);
}