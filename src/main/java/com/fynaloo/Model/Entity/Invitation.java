package com.fynaloo.Model.Entity;

import com.fynaloo.Model.Enum.InvitationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "invitaions")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Group group;
    @ManyToOne
    private User invitedUser;

    @ManyToOne
    private User invitedBy;

    @Enumerated(EnumType.STRING)
    private InvitationStatus status;

    @Column(nullable = false, unique = true)
    private String token;

    private LocalDateTime createdAt;

    public Invitation() {
        this.status = InvitationStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }
}
