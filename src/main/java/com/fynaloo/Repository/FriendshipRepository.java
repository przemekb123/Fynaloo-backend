package com.fynaloo.Repository;

import com.fynaloo.Model.Entity.Friendship;
import com.fynaloo.Model.Entity.User;
import com.fynaloo.Model.Enum.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    Optional<Friendship> findBySenderAndReceiver(User sender, User receiver);

    List<Friendship> findAllBySenderOrReceiverAndStatus(User sender, User receiver, FriendshipStatus status);

    List<Friendship> findAllByReceiverAndStatus(User receiver, FriendshipStatus status);

    List<Friendship> findAllBySenderAndStatus(User sender, FriendshipStatus status);

}
