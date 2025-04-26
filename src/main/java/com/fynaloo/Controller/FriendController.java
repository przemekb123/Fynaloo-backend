package com.fynaloo.Controller;

import com.fynaloo.Dto.*;
import com.fynaloo.Service.IFriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {

    private final IFriendService friendService;

    @PostMapping("/request")
    public ResponseEntity<Void> sendFriendRequest(@RequestBody FriendAddRequest request) {
        friendService.sendFriendRequest(request.getUsername());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/accept")
    public ResponseEntity<Void> acceptFriendRequest(@RequestBody FriendActionRequest request) {
        friendService.acceptFriendRequest(request.getUserId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reject")
    public ResponseEntity<Void> rejectFriendRequest(@RequestBody FriendActionRequest request) {
        friendService.rejectFriendRequest(request.getUserId());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<FriendDTO>> listFriends() {
        List<FriendDTO> friends = friendService.listFriends();
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/requests")
    public ResponseEntity<List<FriendRequest>> listPendingRequests() {
        List<FriendRequest> pendingRequests = friendService.listPendingRequests();
        return ResponseEntity.ok(pendingRequests);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse> removeFriend(@RequestBody FriendActionRequest request){
        friendService.removeFriend(request.getUserId());
        return ResponseEntity.ok(new ApiResponse("Friend removed successfull "));
    }
}
