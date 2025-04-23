package com.fynaloo.Service.Impl;

import com.fynaloo.Dto.CreateGroupRequest;
import com.fynaloo.Dto.GroupDetailsDTO;
import com.fynaloo.Mapper.GroupMapper;
import com.fynaloo.Model.Entity.Group;
import com.fynaloo.Model.Entity.GroupMember;
import com.fynaloo.Model.Entity.User;
import com.fynaloo.Model.Enum.GroupRole;
import com.fynaloo.Repository.GroupMemberRepository;
import com.fynaloo.Repository.GroupRepository;
import com.fynaloo.Repository.UserRepository;
import com.fynaloo.Service.Impl.GroupServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    private GroupRepository groupRepository;
    @Mock
    private GroupMemberRepository groupMemberRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GroupMapper groupMapper;

    @InjectMocks
    private GroupServiceImpl groupService;

    private User user;
    private Group group;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("creator");

        group = new Group();
        group.setId(1L);
        group.setGroupName("Test Group");
        group.setMembers(new HashSet<>());


    }

    @Test
    void should_create_group_successfully() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(groupRepository.save(any(Group.class))).thenAnswer(i -> i.getArguments()[0]);
        when(groupMapper.toGroupDTO(any(Group.class))).thenReturn(new GroupDetailsDTO());

        // when
        GroupDetailsDTO result = groupService.createGroup(new CreateGroupRequest(1L,"creator"));

        // then
        assertThat(result).isNotNull();
        verify(userRepository).findById(1L);
        verify(groupRepository).save(any(Group.class));
        verify(groupMemberRepository).save(any(GroupMember.class));
        verify(groupMapper).toGroupDTO(any(Group.class));
    }

    @Test
    void should_add_member_to_group() {
        // given
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));

        User user = new User();
        user.setId(2L);
        user.setUsername("creator");

        when(userRepository.findByUsername("creator")).thenReturn(Optional.of(user));

        // when
        groupService.addMemberToGroup(1L, "creator");

        // then
        verify(groupRepository).findById(1L);
        verify(userRepository).findByUsername("creator");
        verify(groupMemberRepository).save(any(GroupMember.class));
    }

    @Test
    void should_throw_exception_when_user_already_in_group() {
        // given
        GroupMember member = new GroupMember();
        User existingUser = new User();
        existingUser.setId(2L);
        existingUser.setUsername("creator");
        member.setUser(existingUser);
        group.setMembers(Set.of(member));

        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(userRepository.findByUsername("creator")).thenReturn(Optional.of(existingUser));

        // when / then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            groupService.addMemberToGroup(1L, "creator");
        });

        assertThat(exception.getMessage()).isEqualTo("User is already a member of this group.");
    }


    @Test
    void should_remove_member_from_group() {
        // given
        GroupMember member = new GroupMember();
        member.setId(10L);

        when(groupMemberRepository.findByGroupIdAndUserId(1L, 2L)).thenReturn(Optional.of(member));

        // when
        groupService.removeMemberFromGroup(1L, 2L);

        // then
        verify(groupMemberRepository).delete(member);
    }

    @Test
    void should_get_groups_for_user() {
        // given
        when(groupRepository.findGroupsByUserId(1L)).thenReturn(List.of(group));
        when(groupMapper.toGroupDTO(group)).thenReturn(new GroupDetailsDTO());

        // when
        List<GroupDetailsDTO> groups = groupService.getGroupsForUser(1L);

        // then
        assertThat(groups).isNotEmpty();
        verify(groupRepository).findGroupsByUserId(1L);
        verify(groupMapper).toGroupDTO(group);
    }

    @Test
    void should_get_group_details() {
        // given
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(groupMapper.toGroupDTO(group)).thenReturn(new GroupDetailsDTO());

        // when
        GroupDetailsDTO result = groupService.getGroupDetails(1L);

        // then
        assertThat(result).isNotNull();
        verify(groupRepository).findById(1L);
        verify(groupMapper).toGroupDTO(group);
    }
}
