package ru.litvak.userservice.service;

import ru.litvak.userservice.model.dto.FriendRequestDto;
import ru.litvak.userservice.model.request.FriendIdRequest;
import ru.litvak.userservice.model.response.GetFriendsResponse;

import java.util.List;
import java.util.UUID;

public interface FriendService {
    GetFriendsResponse getFriends(String authHeader, UUID userId);

    void sendFriendRequest(String authHeader, FriendIdRequest request);

    List<FriendRequestDto> getFriendRequest(String authHeader, boolean incoming);

    void acceptFriendRequest(String authHeader, Long requestId);

    void deleteFriendRequest(String authHeader, Long requestId, Boolean isCanceled);
}
