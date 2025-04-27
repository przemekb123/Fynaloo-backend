package com.fynaloo.Controller;

import com.fynaloo.Dto.*;
import com.fynaloo.Service.IGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final IGroupService groupService;

    @PostMapping
    public ResponseEntity<GroupDetailsDTO> createGroup(@RequestBody CreateGroupRequest request) {
        GroupDetailsDTO group = groupService.createGroup(request);
        return ResponseEntity.ok(group);
    }


    @DeleteMapping("/{groupId}/remove-member/{userId}")
    public ResponseEntity<ApiResponse> removeMemberFromGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        groupService.removeMemberFromGroup(groupId, userId);
        return ResponseEntity.ok(new ApiResponse("Member removed successfully"));
    }

    @DeleteMapping("/delete/{groupId}")
    public ResponseEntity<ApiResponse> removeGroup(@PathVariable Long groupId) {
        groupService.deleteGroup(groupId);
        return ResponseEntity.ok(new ApiResponse("Group removed successfully"));
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupDetailsDTO> getGroupDetails(@PathVariable Long groupId) {
        GroupDetailsDTO group = groupService.getGroupDetails(groupId);
        return ResponseEntity.ok(group);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GroupDetailsDTO>> getGroupsForUser(@PathVariable Long userId) {
        List<GroupDetailsDTO> groups = groupService.getGroupsForUser(userId);
        return ResponseEntity.ok(groups);
    }

    @PostMapping("/{groupId}/invite")
    public ResponseEntity<ApiResponse> inviteUserToGroup(@PathVariable Long groupId, @RequestBody InviteRequest request) {
        groupService.inviteUserToGroup(groupId, request.getUsername());
        return ResponseEntity.ok(new ApiResponse("Invitation sent successfully"));
    }

    @PostMapping("/invitations/{invitationId}/accept")
    public ResponseEntity<Void> acceptInvitation(@PathVariable Long invitationId) {
        groupService.acceptGroupInvitation(invitationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/invitations/{invitationId}/reject")
    public ResponseEntity<Void> rejectInvitation(@PathVariable Long invitationId) {
        groupService.rejectGroupInvitation(invitationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/join/{groupUrl}")
    public ResponseEntity<ApiResponse> joinGroupViaLink(@PathVariable String groupUrl) {
        groupService.joinGroupViaLink(groupUrl);
        return ResponseEntity.ok(new ApiResponse("Invitation accepted"));
    }

    @GetMapping("/invitations")
    public ResponseEntity<List<InvitationDTO>> getPendingInvitations() {
        return ResponseEntity.ok(groupService.listPendingInvitations());
    }


}
