package com.fynaloo.Service;

import com.fynaloo.Dto.CreateGroupRequest;
import com.fynaloo.Dto.GroupDetailsDTO;
import com.fynaloo.Dto.InvitationDTO;

import java.util.List;

public interface IGroupService {

    GroupDetailsDTO createGroup(CreateGroupRequest request);

    void addMemberToGroup(Long groupId, String username);

    void removeMemberFromGroup(Long groupId, Long userId);

    List<GroupDetailsDTO> getGroupsForUser(Long userId);

    GroupDetailsDTO getGroupDetails(Long groupId);
    void inviteUserToGroup(Long groupId, String username);
    void acceptGroupInvitation(Long groupId);
    void rejectGroupInvitation(Long groupId);
    void joinGroupViaLink(String groupUrl);
    List<InvitationDTO> listPendingInvitations();
    void deleteGroup(Long groupId);
}
