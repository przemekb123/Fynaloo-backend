package com.fynaloo.Repository;

import com.fynaloo.Model.Entity.Invitation;
import com.fynaloo.Model.Enum.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findByInvitedUserIdAndStatus(Long userId, InvitationStatus status);
}