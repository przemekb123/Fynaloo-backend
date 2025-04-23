package com.fynaloo.Mapper;

import com.fynaloo.Dto.GroupDetailsDTO;
import com.fynaloo.Dto.MemberInfoDTO;
import com.fynaloo.Model.Entity.Group;
import com.fynaloo.Model.Entity.GroupMember;
import com.fynaloo.Model.Entity.User;
import com.fynaloo.Model.Enum.GroupRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GroupMapperTest {

    private GroupMapper groupMapper;


    @BeforeEach
    void setUp() {
        groupMapper = new GroupMapperImpl(); // MapStruct generuje tę klasę automatycznie
    }

    @Test
    void should_map_group_to_group_details_DTO (){
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");

        GroupMember groupMember = new GroupMember();
        groupMember.setUser(user);
        groupMember.setRole(GroupRole.MEMBER);

        Set<GroupMember> members = new HashSet<>();
        members.add(groupMember);

        Group group = new Group();
        group.setId(100L);
        group.setGroupName("Test Group");
        group.setGroupUrl("test-group-url");
        group.setMembers(members);

        // when
        GroupDetailsDTO dto = groupMapper.toGroupDTO(group);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getGroupId()).isEqualTo(100L);
        assertThat(dto.getGroupName()).isEqualTo("Test Group");
        assertThat(dto.getGroupUrl()).isEqualTo("test-group-url");
        assertThat(dto.getMembers()).hasSize(1);

        MemberInfoDTO memberDTO = dto.getMembers().iterator().next();
        assertThat(memberDTO.getUserId()).isEqualTo(1L);
        assertThat(memberDTO.getUsername()).isEqualTo("testuser");
        assertThat(memberDTO.getEmail()).isEqualTo("testuser@example.com");
        assertThat(memberDTO.getRole()).isEqualTo("MEMBER");
    }

    @Test
    void should_map_group_member_to_member_info_DTO() {
        // given
        User user = new User();
        user.setId(2L);
        user.setUsername("anotheruser");
        user.setEmail("anotheruser@example.com");

        GroupMember groupMember = new GroupMember();
        groupMember.setUser(user);
        groupMember.setRole(GroupRole.ADMIN);

        // when
        MemberInfoDTO memberDTO = groupMapper.toMemberInfoDTO(groupMember);

        // then
        assertThat(memberDTO).isNotNull();
        assertThat(memberDTO.getUserId()).isEqualTo(2L);
        assertThat(memberDTO.getUsername()).isEqualTo("anotheruser");
        assertThat(memberDTO.getEmail()).isEqualTo("anotheruser@example.com");
        assertThat(memberDTO.getRole()).isEqualTo("ADMIN");
    }

}