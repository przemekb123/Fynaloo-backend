package com.fynaloo.Repository;

import com.fynaloo.Model.Entity.ManualDebt;
import com.fynaloo.Model.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ManualDebtRepository extends JpaRepository<ManualDebt, Long> {
    List<ManualDebt> findByDebtorId(Long userId);

    List<ManualDebt> findByDebtorIdAndCreditorId(Long debtorId, Long creditorId);

    @Query("""
            SELECT d FROM ManualDebt d
            WHERE d.settled = false
              AND ((d.debtor = :debtor AND d.creditor = :creditor)
                OR (d.debtor = :creditor AND d.creditor = :debtor))
            """)
    Optional<ManualDebt> findDebtBetween(@Param("debtor") User debtor, @Param("creditor") User creditor);
}