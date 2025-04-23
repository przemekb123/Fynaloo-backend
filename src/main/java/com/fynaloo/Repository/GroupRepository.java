package com.fynaloo.Repository;

import com.fynaloo.Model.Entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query("SELECT gm.group FROM GroupMember gm WHERE gm.user.id = :userId")
    List<Group> findGroupsByUserId(Long userId);

    Optional<Group> findByGroupUrl(String groupUrl);
}