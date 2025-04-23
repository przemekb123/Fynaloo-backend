package com.fynaloo.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDetailsDTO {
    private Long groupId;
    private String groupName;
    private String groupUrl;
    private Set<MemberInfoDTO> members;

}
