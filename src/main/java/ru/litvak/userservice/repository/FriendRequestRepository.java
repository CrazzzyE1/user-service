package ru.litvak.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.litvak.userservice.enumerated.FriendRequestStatus;
import ru.litvak.userservice.model.entity.FriendRequest;
import ru.litvak.userservice.model.entity.UserProfile;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    boolean existsBySenderAndReceiverAndStatus(UserProfile sender, UserProfile receiver, FriendRequestStatus status);

    Optional<FriendRequest> findByIdAndReceiverAndStatus(Long id, UserProfile receiver, FriendRequestStatus status);

    List<FriendRequest> findBySenderAndStatus(UserProfile sender, FriendRequestStatus status);

    List<FriendRequest> findByReceiverAndStatus(UserProfile sender, FriendRequestStatus status);
}
