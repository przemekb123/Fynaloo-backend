package com.fynaloo.Mapper;

import com.fynaloo.Dto.GroupDetailsDTO;
import com.fynaloo.Dto.MemberInfoDTO;
import com.fynaloo.Model.Entity.Group;
import com.fynaloo.Model.Entity.GroupMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;

    @Mapper(componentModel = "spring")
    public interface GroupMapper {

        @Mapping(source = "id", target = "groupId")
        GroupDetailsDTO toGroupDTO(Group group);

        @Mapping(source = "user.id", target = "userId")
        @Mapping(source = "user.username", target = "username")
        @Mapping(source = "user.email", target = "email")
        @Mapping(source = "role", target = "role", qualifiedByName = "enumToString")
        MemberInfoDTO toMemberInfoDTO(GroupMember groupMember);

        @Named("enumToString")
        default String enumToString(Enum<?> e) {
            return e != null ? e.name() : null;
        }
    }

