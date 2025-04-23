package com.fynaloo.Mapper;

import com.fynaloo.Dto.InvitationDTO;
import com.fynaloo.Model.Entity.Invitation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface InvitationMapper {

    @Mapping(source = "group.groupName", target = "groupName")
    @Mapping(source = "invitedBy.username", target = "invitedByUsername")
    @Mapping(source = "token", target = "token")
    InvitationDTO toInvitationDTO(Invitation invitation);

}
