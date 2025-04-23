package com.fynaloo.Service.Impl;

import com.fynaloo.Dto.CreateGroupRequest;
import com.fynaloo.Dto.GroupDetailsDTO;
import com.fynaloo.Dto.InvitationDTO;
import com.fynaloo.Mapper.GroupMapper;
import com.fynaloo.Mapper.InvitationMapper;
import com.fynaloo.Model.Entity.Group;
import com.fynaloo.Model.Entity.GroupMember;
import com.fynaloo.Model.Entity.Invitation;
import com.fynaloo.Model.Entity.User;
import com.fynaloo.Model.Enum.GroupRole;
import com.fynaloo.Model.Enum.InvitationStatus;
import com.fynaloo.Repository.GroupMemberRepository;
import com.fynaloo.Repository.GroupRepository;
import com.fynaloo.Repository.InvitationRepository;
import com.fynaloo.Repository.UserRepository;
import com.fynaloo.Service.IGroupService;
import com.fynaloo.Service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupServiceImpl implements IGroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;
    private final GroupMapper groupMapper;
    private final InvitationMapper invitationMapper;
    private final IUserService userService;
    private final InvitationRepository invitationRepository;

    @Override
    public GroupDetailsDTO createGroup(CreateGroupRequest request) {
        User creator = userRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Group group = new Group();
        group.setGroupName(request.getGroupName());
        group.setGroupUrl(generateGroupUrl(request.getGroupName()));

        group = groupRepository.save(group);

        GroupMember groupMember = new GroupMember();
        groupMember.setGroup(group);
        groupMember.setUser(creator);
        groupMember.setRole(GroupRole.ADMIN);

        groupMemberRepository.save(groupMember);

        group.getMembers().add(groupMember);

        return groupMapper.toGroupDTO(group);
    }

    @Override
    public void addMemberToGroup(Long groupId, String username) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean alreadyMember = group.getMembers().stream()
                .anyMatch(member -> member.getUser().getUsername().equals(username));

        if (alreadyMember) {
            throw new RuntimeException("User is already a member of this group.");
        }

        GroupMember groupMember = new GroupMember();
        groupMember.setGroup(group);
        groupMember.setUser(user);
        groupMember.setRole(GroupRole.MEMBER);

        groupMemberRepository.save(groupMember);

        group.getMembers().add(groupMember);
    }

    @Override
    public void removeMemberFromGroup(Long groupId, Long userId) {
        GroupMember member = groupMemberRepository.findByGroupIdAndUserId(groupId, userId)
                .orElseThrow(() -> new RuntimeException("Member not found in group"));

        groupMemberRepository.delete(member);
    }

    @Override
    public List<GroupDetailsDTO> getGroupsForUser(Long userId) {
        List<Group> groups = groupRepository.findGroupsByUserId(userId);
        return groups.stream()
                .map(groupMapper::toGroupDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GroupDetailsDTO getGroupDetails(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        return groupMapper.toGroupDTO(group);
    }

    private String generateGroupUrl(String groupName) {
        return groupName.toLowerCase().replaceAll("\\s+", "-") + "-" + System.currentTimeMillis();
    }

    @Override
    @Transactional
    public void inviteUserToGroup(Long groupId, String username) {
        User invitedUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User inviter = userService.getCurrentUser();

        boolean alreadyInvited = invitationRepository.findByInvitedUserIdAndStatus(invitedUser.getId(), InvitationStatus.PENDING)
                .stream()
                .anyMatch(inv -> inv.getGroup().getId().equals(groupId));
        if (alreadyInvited) {
            throw new RuntimeException("User already invited");
        }

        Invitation invitation = Invitation.builder()
                .group(group)
                .invitedUser(invitedUser)
                .invitedBy(inviter)
                .status(InvitationStatus.PENDING)
                .token(UUID.randomUUID().toString())
                .build();

        invitationRepository.save(invitation);
    }

    @Override
    public List<InvitationDTO> listPendingInvitations() {
        User currentUser = userService.getCurrentUser();
        return invitationRepository.findByInvitedUserIdAndStatus(currentUser.getId(), InvitationStatus.PENDING)
                .stream()
                .map(invitationMapper::toInvitationDTO)
                .toList();
    }

    @Override
    @Transactional
    public void acceptGroupInvitation(Long invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new RuntimeException("Invitation not found"));

        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new RuntimeException("Invitation is not pending");
        }

        GroupMember member = new GroupMember();
        member.setGroup(invitation.getGroup());
        member.setUser(invitation.getInvitedUser());
        member.setRole(GroupRole.MEMBER);
        groupMemberRepository.save(member);

        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitationRepository.save(invitation);
    }

    @Override
    @Transactional
    public void rejectGroupInvitation(Long invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new RuntimeException("Invitation not found"));

        invitation.setStatus(InvitationStatus.REJECTED);
        invitationRepository.save(invitation);
    }
    @Override
    public void joinGroupViaLink(String groupUrl) {
        Long currentUserId = userService.getCurrentUser().getId();

        Group group = groupRepository.findByGroupUrl(groupUrl)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        boolean alreadyMember = group.getMembers().stream()
                .anyMatch(member -> member.getUser().getId().equals(currentUserId));

        if (alreadyMember) {
            throw new RuntimeException("Already a member");
        }

        GroupMember member = new GroupMember();
        member.setGroup(group);
        member.setUser(userRepository.findById(currentUserId).orElseThrow(() -> new RuntimeException("User not found")));
        member.setRole(GroupRole.MEMBER);

        groupMemberRepository.save(member);

        group.getMembers().add(member);
    }

    @Override
    public void deleteGroup(Long groupId) {
        User currentUser = userService.getCurrentUser();

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        boolean isAdmin = group.getMembers().stream()
                .anyMatch(member ->
                        member.getUser().getId().equals(currentUser.getId()) &&
                                member.getRole() == GroupRole.ADMIN
                );

        if (!isAdmin) {
            throw new RuntimeException("Only group admin can delete the group");
        }


        groupRepository.delete(group);
    }
}
