package ru.litvak.userservice.manager.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.litvak.userservice.event.EventType;
import ru.litvak.userservice.event.EventDto;
import ru.litvak.userservice.event.NotificationMethod;
import ru.litvak.userservice.exception.NotFoundException;
import ru.litvak.userservice.exception.RequestParameterException;
import ru.litvak.userservice.manager.FriendRequestManager;
import ru.litvak.userservice.model.entity.FriendRequest;
import ru.litvak.userservice.model.entity.UserProfile;
import ru.litvak.userservice.repository.FriendRequestRepository;
import ru.litvak.userservice.repository.UserProfileRepository;
import ru.litvak.userservice.service.KafkaEventPublisher;

import java.util.List;
import java.util.UUID;

import static ru.litvak.userservice.enumerated.FriendRequestStatus.*;
import static ru.litvak.userservice.event.EventType.FRIENDS_ACCEPT;
import static ru.litvak.userservice.event.EventType.FRIENDS_REQUEST;
import static ru.litvak.userservice.event.NotificationMethod.APP_BELL;

@Service
@RequiredArgsConstructor
public class FriendRequestManagerImpl implements FriendRequestManager {

    @Value(value = "${notifications.topic.friends}")
    private String TOPIC_FRIENDS;

    private final FriendRequestRepository friendRequestRepository;
    private final UserProfileRepository userProfileRepository;
    private final KafkaEventPublisher publisher;

    @Override
    @Transactional
    public void accept(UUID me, Long id) {
        UserProfile userProfile = userProfileRepository.findById(me)
                .orElseThrow(() -> new NotFoundException("Profile with id %s not found".formatted(me)));
        FriendRequest request = friendRequestRepository.findByIdAndReceiverAndStatus(id, userProfile, PENDING)
                .orElseThrow(() -> new NotFoundException("FriendRequest with id %s and status PENDING not found".formatted(id)));
        UserProfile friendProfile = request.getSender();
        request.setStatus(ACCEPTED);
        userProfile.getFriends().add(friendProfile);
        friendProfile.getFriends().add(userProfile);

        sendNotification(request.getId(), me, friendProfile.getId(), FRIENDS_ACCEPT, List.of(APP_BELL));
    }

    @Override
    @Transactional
    public void create(UUID me, UUID friendId) {
        if (me.equals(friendId)) {
            throw new RequestParameterException("friendId", "Cannot send friend request to yourself");
        }

        UserProfile sender = userProfileRepository.findById(me)
                .orElseThrow(() -> new NotFoundException("Sender profile not found with id: " + me));
        UserProfile receiver = userProfileRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Receiver profile not found with id: " + friendId));

        if (friendRequestRepository.existsBySenderAndReceiverAndStatus(
                sender, receiver, PENDING)) {
            throw new RuntimeException("Friend request already exists");
        }
        if (sender.getFriends().contains(receiver)) {
            throw new RuntimeException("Users are already friends");
        }

        FriendRequest saved = friendRequestRepository.save(FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .build());

        sendNotification(saved.getId(), me, friendId, FRIENDS_REQUEST, List.of(APP_BELL));
    }

    @Override
    public List<FriendRequest> get(UUID me, boolean incoming) {
        UserProfile profile = userProfileRepository.findById(me)
                .orElseThrow(() -> new NotFoundException("Profile with id %s not found".formatted(me)));
        if (incoming) {
            return friendRequestRepository.findByReceiverAndStatus(profile, PENDING);
        }
        return friendRequestRepository.findBySenderAndStatus(profile, PENDING);

    }

    @Override
    @Transactional
    public void delete(UUID me, Long id, Boolean isCanceled) {
        UserProfile userProfile = userProfileRepository.findById(me)
                .orElseThrow(() -> new NotFoundException("Profile with id %s not found".formatted(me)));
        if (isCanceled) {
            FriendRequest request = friendRequestRepository.findByIdAndReceiverAndStatus(id, userProfile, PENDING)
                    .orElseThrow(() -> new NotFoundException("FriendRequest with id %s and status PENDING not found".formatted(id)));
            request.setStatus(CANCELLED);
            return;
        }
        FriendRequest request = friendRequestRepository.findByIdAndSenderAndStatus(id, userProfile, PENDING)
                .orElseThrow(() -> new NotFoundException("FriendRequest with id %s and status PENDING not found".formatted(id)));
        request.setStatus(REJECTED);
    }

    private void sendNotification(Long entityId, UUID me, UUID friendId, EventType eventType,
                                  List<NotificationMethod> notificationMethods) {
        EventDto event = EventDto.builder()
                .senderId(me)
                .recipientId(friendId)
                .type(eventType)
                .entityId(String.valueOf(entityId))
                .methods(notificationMethods)
                .build();

        publisher.publish(TOPIC_FRIENDS, String.valueOf(event.getId()), event, EventDto.class);
    }
}
