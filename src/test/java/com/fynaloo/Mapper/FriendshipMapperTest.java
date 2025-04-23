package com.fynaloo.Mapper;

import com.fynaloo.Dto.FriendDTO;
import com.fynaloo.Dto.FriendRequest;
import com.fynaloo.Model.Entity.Friendship;
import com.fynaloo.Model.Enum.FriendshipStatus;
import com.fynaloo.Model.Entity.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.assertj.core.api.Assertions.assertThat;

class FriendshipMapperTest {

    private final FriendshipMapper mapper = Mappers.getMapper(FriendshipMapper.class);

    @Test
    void should_map_friendship_to_friend_request_dto() {
        // given
        User sender = new User();
        sender.setId(1L);
        sender.setUsername("sender_user");

        User receiver = new User();
        receiver.setId(2L);
        receiver.setUsername("receiver_user");

        Friendship friendship = new Friendship();
        friendship.setSender(sender);
        friendship.setReceiver(receiver);
        friendship.setStatus(FriendshipStatus.PENDING);

        // when
        FriendRequest dto = mapper.toFriendRequestDTO(friendship);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getSenderId()).isEqualTo(1L);
        assertThat(dto.getSenderUsername()).isEqualTo("sender_user");
        assertThat(dto.getReceiverId()).isEqualTo(2L);
        assertThat(dto.getReceiverUsername()).isEqualTo("receiver_user");
    }

    @Test
    void should_map_user_to_friend_dto() {
        // given
        User user = new User();
        user.setId(3L);
        user.setUsername("friend_user");
        user.setEmail("friend@example.com");
        user.setFirstName("Friend");
        user.setLastName("User");

        // when
        FriendDTO dto = mapper.toFriendDTO(user);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(3L);
        assertThat(dto.getUsername()).isEqualTo("friend_user");
        assertThat(dto.getEmail()).isEqualTo("friend@example.com");
        assertThat(dto.getFirstName()).isEqualTo("Friend");
        assertThat(dto.getLastName()).isEqualTo("User");
    }
}
