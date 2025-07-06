package ru.litvak.userservice.manager.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.litvak.userservice.enumerated.FriendRequestStatus;
import ru.litvak.userservice.exception.NotFoundException;
import ru.litvak.userservice.exception.RequestParameterException;
import ru.litvak.userservice.manager.FriendManager;
import ru.litvak.userservice.manager.UserProfileManager;
import ru.litvak.userservice.model.entity.FriendRequest;
import ru.litvak.userservice.model.entity.UserProfile;
import ru.litvak.userservice.model.response.GetFriendsResponse;
import ru.litvak.userservice.repository.FriendRequestRepository;
import ru.litvak.userservice.repository.UserProfileRepository;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FriendManagerImpl implements FriendManager {

    private final UserProfileManager userProfileManager;
    private final UserProfileRepository userProfileRepository;
    private final FriendRequestRepository friendRequestRepository;

    @Override
    public GetFriendsResponse getFriends(UUID me, UUID userId) {
        UserProfile userProfile = userProfileManager.getUserProfile(me, userId);
        return new GetFriendsResponse(userProfile.getFriends().stream()
                .map(UserProfile::getId)
                .toList());
    }

    @Override
    @Transactional
    public void createFriendRequest(UUID me, UUID friendId) {
        if (me.equals(friendId)) {
            throw new RequestParameterException("friendId", "Cannot send friend request to yourself");
        }

        UserProfile sender = userProfileRepository.findById(me)
                .orElseThrow(() -> new NotFoundException("Sender profile not found with id: " + me));
        UserProfile receiver = userProfileRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Receiver profile not found with id: " + friendId));

        if (friendRequestRepository.existsBySenderAndReceiverAndStatus(
                sender, receiver, FriendRequestStatus.PENDING)) {
            throw new RuntimeException("Friend request already exists");
        }
        if (sender.getFriends().contains(receiver)) {
            throw new RuntimeException("Users are already friends");
        }

        FriendRequest request = FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .build();
        friendRequestRepository.save(request);
    }
}
