package com.fynaloo.Mapper;

import com.fynaloo.Dto.MemberInfoDTO;
import com.fynaloo.Dto.UserDetailsDTO;
import com.fynaloo.Dto.GroupDetailsDTO;
import com.fynaloo.Model.Entity.User;
import com.fynaloo.Model.Entity.GroupMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {


    @Mapping(target = "debts", ignore = true) // debts ustawiamy w serwisie
    @Mapping(source = "groupMemberships", target = "groups", qualifiedByName = "groupMembershipsToGroupDetails")
    UserDetailsDTO toUserDetailsDTO(User user);

    @Named("groupMembershipsToGroupDetails")
    default Set<GroupDetailsDTO> groupMembershipsToGroupDetails(Set<GroupMember> memberships) {
        return memberships.stream()
                .map(m -> {
                    var group = m.getGroup();
                    var members = group.getMembers().stream()
                            .map(this::toMemberInfoDTO)
                            .collect(Collectors.toSet());

                    return new GroupDetailsDTO(
                            group.getId(),
                            group.getGroupName(),
                            group.getGroupUrl(),
                            members
                    );
                })
                .collect(Collectors.toSet());
    }

    default MemberInfoDTO toMemberInfoDTO(GroupMember member) {
        return new MemberInfoDTO(
                member.getUser().getId(),
                member.getUser().getUsername(),
                member.getUser().getEmail(),
                member.getRole().name()
        );
    }
}




