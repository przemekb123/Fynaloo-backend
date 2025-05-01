package com.fynaloo.Repository;

import com.fynaloo.Model.Entity.Balance;
import com.fynaloo.Model.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, Long> {

    @Query("SELECT b FROM Balance b WHERE (b.user1 = :userA AND b.counterparty = :userB) OR (b.user1 = :userB AND b.counterparty = :userA)")
    Optional<Balance> findByUsers(@Param("userA") User userA, @Param("userB") User userB);

    @Query("""
    SELECT b FROM Balance b
    WHERE b.user1 = :user OR b.counterparty = :user
""")
    List<Balance> findAllByUser(@Param("user") User user);


}