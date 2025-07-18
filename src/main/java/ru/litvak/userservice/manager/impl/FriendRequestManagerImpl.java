package ru.litvak.userservice.manager.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.litvak.userservice.event.NotificationEvent;
import ru.litvak.userservice.exception.NotFoundException;
import ru.litvak.userservice.exception.RequestParameterException;
import ru.litvak.userservice.manager.FriendRequestManager;
import ru.litvak.userservice.model.entity.FriendRequest;
import ru.litvak.userservice.model.entity.UserProfile;
import ru.litvak.userservice.repository.FriendRequestRepository;
import ru.litvak.userservice.repository.UserProfileRepository;
import ru.litvak.userservice.service.KafkaEventPublisher;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static ru.litvak.userservice.enumerated.FriendRequestStatus.*;
import static ru.litvak.userservice.event.EventType.FRIENDS_REQUEST;
import static ru.litvak.userservice.event.NotificationMethod.APP_BELL;

@Service
@RequiredArgsConstructor
public class FriendRequestManagerImpl implements FriendRequestManager {

    private final FriendRequestRepository friendRequestRepository;
    private final UserProfileRepository userProfileRepository;
    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;
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

        FriendRequest request = FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .build();

        FriendRequest saved = friendRequestRepository.save(request);
        NotificationEvent event = NotificationEvent.builder()
                .eventId(UUID.randomUUID())
                .senderId(me)
                .recipientId(friendId)
                .eventType(FRIENDS_REQUEST)
                .entityId(String.valueOf(saved.getId()))
                .eventDateTime(Instant.now())
                .notificationMethods(List.of(APP_BELL))
                .build();

        publisher.publish("notifications.friends", String.valueOf(event.getEventId()), event, NotificationEvent.class);
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
}
