package com.fynaloo.Service;

import com.fynaloo.Dto.FriendDTO;
import com.fynaloo.Dto.FriendRequest;

import java.util.List;

public interface IFriendService {
    void sendFriendRequest(String username);

    void acceptFriendRequest(Long senderId);

    void rejectFriendRequest(Long senderId);

    List<FriendDTO> listFriends();

    List<FriendRequest> listPendingRequests();
}
