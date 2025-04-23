package com.fynaloo.Mapper;

import com.fynaloo.Dto.GroupDetailsDTO;
import com.fynaloo.Dto.UserDetailsDTO;
import com.fynaloo.Model.Entity.Group;
import com.fynaloo.Model.Entity.GroupMember;
import com.fynaloo.Model.Entity.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void should_map_user_to_UserDetailsDTO() {

        // given
        User user = new User();
        user.setId(1L);
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setUsername("janek123");
        user.setPassword("secret");
        user.setRegistrationDate(LocalDateTime.now());

        // Dodajemy grupy
        Group group = new Group();
        group.setId(10L);
        group.setGroupName("Wyjazd 2025");
        group.setGroupUrl("abc123");

        GroupMember groupMember = new GroupMember();
        groupMember.setGroup(group);
        groupMember.setUser(user);

        user.setGroupMemberships(Set.of(groupMember));

        // when
        UserDetailsDTO dto = userMapper.toUserDetailsDTO(user);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getFirstName()).isEqualTo("Jan");
        assertThat(dto.getLastName()).isEqualTo("Kowalski");
        assertThat(dto.getUsername()).isEqualTo("janek123");

        assertThat(dto.getGroups()).isNotNull();
        assertThat(dto.getGroups()).hasSize(1);

        GroupDetailsDTO groupDTO = dto.getGroups().iterator().next();
        assertThat(groupDTO.getGroupId()).isEqualTo(10L);
        assertThat(groupDTO.getGroupName()).isEqualTo("Wyjazd 2025");
        assertThat(groupDTO.getGroupUrl()).isEqualTo("abc123");

        assertThat(dto.getDebts()).isNull(); // debts są ustawiane w serwisie, nie przez mapper
    }


}