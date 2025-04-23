package com.fynaloo.Model.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fynaloo.Model.Enum.GroupRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "group_members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    @JsonBackReference
    private Group group;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GroupRole role = GroupRole.MEMBER;


}
