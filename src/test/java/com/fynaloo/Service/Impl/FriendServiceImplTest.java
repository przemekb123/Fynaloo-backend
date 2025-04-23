package com.fynaloo.Service.Impl;

import com.fynaloo.Dto.FriendDTO;
import com.fynaloo.Dto.FriendRequest;
import com.fynaloo.Mapper.FriendshipMapper;
import com.fynaloo.Model.Entity.Friendship;
import com.fynaloo.Model.Enum.FriendshipStatus;
import com.fynaloo.Model.Entity.User;
import com.fynaloo.Repository.FriendshipRepository;
import com.fynaloo.Repository.UserRepository;
import com.fynaloo.Security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FriendServiceImplTest{
        @Mock
        private UserRepository userRepository;

        @Mock
        private FriendshipRepository friendshipRepository;

        @Mock
        private FriendshipMapper friendshipMapper;

        @InjectMocks
        private FriendServiceImpl friendService;

        private User currentUser;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);

            currentUser = new User();
            currentUser.setId(1L);
            currentUser.setUsername("testUser");

            // Ustawianie SecurityContext
            CustomUserDetails userDetails = new CustomUserDetails(currentUser);
            Authentication authentication = mock(Authentication.class);
            when(authentication.getPrincipal()).thenReturn(userDetails);
            when(authentication.isAuthenticated()).thenReturn(true);

            SecurityContext securityContext = mock(SecurityContext.class);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);
        }

    @Test
    void should_send_friend_request_successfully() {
        // given
        User receiver = new User();
        receiver.setId(2L);
        receiver.setUsername("test2");

        when(userRepository.findByUsername("test2")).thenReturn(Optional.of(receiver));
        when(friendshipRepository.findBySenderAndReceiver(currentUser, receiver)).thenReturn(Optional.empty());
        when(friendshipRepository.findBySenderAndReceiver(receiver, currentUser)).thenReturn(Optional.empty());

        // when
        friendService.sendFriendRequest("test2");

        // then
        verify(friendshipRepository).save(any(Friendship.class));
    }

    @Test
    void should_throw_when_friend_request_already_exists() {
        // given
        User receiver = new User();
        receiver.setId(2L);
        receiver.setUsername("test2");

        when(userRepository.findByUsername("test2")).thenReturn(Optional.of(receiver));
        when(friendshipRepository.findBySenderAndReceiver(currentUser, receiver)).thenReturn(Optional.of(new Friendship()));

        // when - then
        assertThatThrownBy(() -> friendService.sendFriendRequest("test2"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Friend request already exists");
    }

        @Test
        void should_accept_friend_request_successfully() {
            // given
            User sender = new User();
            sender.setId(3L);
            Friendship friendship = new Friendship();
            friendship.setSender(sender);
            friendship.setReceiver(currentUser);
            friendship.setStatus(FriendshipStatus.PENDING);

            when(userRepository.findById(3L)).thenReturn(Optional.of(sender));
            when(friendshipRepository.findBySenderAndReceiver(sender, currentUser)).thenReturn(Optional.of(friendship));

            // when
            friendService.acceptFriendRequest(3L);

            // then
            assertThat(friendship.getStatus()).isEqualTo(FriendshipStatus.ACCEPTED);
            verify(friendshipRepository).save(friendship);
        }

        @Test
        void should_reject_friend_request_successfully() {
            // given
            User sender = new User();
            sender.setId(4L);
            Friendship friendship = new Friendship();
            friendship.setSender(sender);
            friendship.setReceiver(currentUser);
            friendship.setStatus(FriendshipStatus.PENDING);

            when(userRepository.findById(4L)).thenReturn(Optional.of(sender));
            when(friendshipRepository.findBySenderAndReceiver(sender, currentUser)).thenReturn(Optional.of(friendship));

            // when
            friendService.rejectFriendRequest(4L);

            // then
            assertThat(friendship.getStatus()).isEqualTo(FriendshipStatus.REJECTED);
            verify(friendshipRepository).save(friendship);
        }

        @Test
        void should_list_friends() {
            // given
            User friendUser = new User();
            friendUser.setId(5L);
            friendUser.setUsername("friend_user");

            Friendship friendship = new Friendship();
            friendship.setSender(currentUser);
            friendship.setReceiver(friendUser);
            friendship.setStatus(FriendshipStatus.ACCEPTED);

            when(friendshipRepository.findAllBySenderOrReceiverAndStatus(currentUser, currentUser, FriendshipStatus.ACCEPTED))
                    .thenReturn(List.of(friendship));

            FriendDTO friendDTO = new FriendDTO();
            when(friendshipMapper.toFriendDTO(friendUser)).thenReturn(friendDTO);

            // when
            List<FriendDTO> friends = friendService.listFriends();

            // then
            assertThat(friends).hasSize(1);
            verify(friendshipMapper).toFriendDTO(friendUser);
        }

        @Test
        void should_list_pending_requests() {
            // given
            Friendship friendship = new Friendship();
            friendship.setSender(new User());
            friendship.setReceiver(currentUser);
            friendship.setStatus(FriendshipStatus.PENDING);

            when(friendshipRepository.findAllByReceiverAndStatus(currentUser, FriendshipStatus.PENDING))
                    .thenReturn(List.of(friendship));

            FriendRequest friendRequest = new FriendRequest();
            when(friendshipMapper.toFriendRequestDTO(friendship)).thenReturn(friendRequest);

            // when
            List<FriendRequest> pendingRequests = friendService.listPendingRequests();

            // then
            assertThat(pendingRequests).hasSize(1);
            verify(friendshipMapper).toFriendRequestDTO(friendship);
        }
}