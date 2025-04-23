package com.fynaloo.Dto;

import lombok.Data;

@Data
public class InvitationDTO {
    private Long id;
    private String groupName;
    private String invitedByUsername;
    private String token;
}
