package com.fynaloo.Service.Impl;

import com.fynaloo.Dto.FriendDTO;
import com.fynaloo.Dto.FriendRequest;
import com.fynaloo.Mapper.FriendshipMapper;
import com.fynaloo.Model.Entity.Friendship;
import com.fynaloo.Model.Entity.User;
import com.fynaloo.Model.Enum.FriendshipStatus;
import com.fynaloo.Repository.FriendshipRepository;
import com.fynaloo.Repository.UserRepository;
import com.fynaloo.Security.CustomUserDetails;
import com.fynaloo.Service.IFriendService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendServiceImpl implements IFriendService {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final FriendshipMapper friendshipMapper;

    @Override
    public void sendFriendRequest(String username) {
        User sender = getCurrentUser();
        User receiver = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        boolean exists = friendshipRepository.findBySenderAndReceiver(sender, receiver).isPresent()
                || friendshipRepository.findBySenderAndReceiver(receiver, sender).isPresent();

        if (exists) {
            throw new RuntimeException("Friend request already exists");
        }

        Friendship friendship = new Friendship();
        friendship.setSender(sender);
        friendship.setReceiver(receiver);
        friendship.setStatus(FriendshipStatus.PENDING);

        friendshipRepository.save(friendship);
    }

    @Override
    public void acceptFriendRequest(Long senderId) {
        User receiver = getCurrentUser();
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        Friendship friendship = friendshipRepository.findBySenderAndReceiver(sender, receiver)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        friendship.setStatus(FriendshipStatus.ACCEPTED);
        friendshipRepository.save(friendship);
    }

    @Override
    public void rejectFriendRequest(Long senderId) {
        User receiver = getCurrentUser();
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        Friendship friendship = friendshipRepository.findBySenderAndReceiver(sender, receiver)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        friendship.setStatus(FriendshipStatus.REJECTED);
        friendshipRepository.save(friendship);
    }

    @Override
    public List<FriendDTO> listFriends() {
        User currentUser = getCurrentUser();

        // Pobieramy tylko zaakceptowane znajomości, w których currentUser jest wysyłającym LUB odbierającym
        List<Friendship> sentAccepted = friendshipRepository.findAllBySenderAndStatus(currentUser, FriendshipStatus.ACCEPTED);
        List<Friendship> receivedAccepted = friendshipRepository.findAllByReceiverAndStatus(currentUser, FriendshipStatus.ACCEPTED);

        return Stream.concat(sentAccepted.stream(), receivedAccepted.stream())
                .map(friendship -> {
                    // Zwracamy drugą osobę w relacji (nie currentUser)
                    if (friendship.getSender().getId().equals(currentUser.getId())) {
                        return friendship.getReceiver();
                    } else {
                        return friendship.getSender();
                    }
                })
                .distinct() // Unikamy duplikatów
                .map(friendshipMapper::toFriendDTO)
                .toList();
    }




    @Override
    public List<FriendRequest> listPendingRequests() {
        User currentUser = getCurrentUser();
        List<Friendship> pending = friendshipRepository.findAllByReceiverAndStatus(currentUser, FriendshipStatus.PENDING);

        return pending.stream()
                .map(friendshipMapper::toFriendRequestDTO)
                .toList();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
            return customUserDetails.getUser();
        }
        throw new IllegalStateException("No authenticated user found");
    }
}