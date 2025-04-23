package com.fynaloo.Repository;

import com.fynaloo.Model.Entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    Optional<GroupMember> findByGroupIdAndUserId(Long groupId, Long userId);
}