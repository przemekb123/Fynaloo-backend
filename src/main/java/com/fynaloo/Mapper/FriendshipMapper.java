package com.fynaloo.Mapper;

import com.fynaloo.Dto.FriendDTO;
import com.fynaloo.Dto.FriendRequest;
import com.fynaloo.Model.Entity.Friendship;
import com.fynaloo.Model.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FriendshipMapper {

    @Mapping(source = "sender.id", target = "senderId")
    @Mapping(source = "sender.username", target = "senderUsername")
    @Mapping(source = "receiver.id", target = "receiverId")
    @Mapping(source = "receiver.username", target = "receiverUsername")
    FriendRequest toFriendRequestDTO(Friendship friendship);

    FriendDTO toFriendDTO(User user);
}
