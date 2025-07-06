package ru.litvak.userservice.service;

import ru.litvak.userservice.model.request.FriendIdRequest;
import ru.litvak.userservice.model.response.GetFriendsResponse;

import java.util.UUID;

public interface FriendService {
    GetFriendsResponse getFriends(String authHeader, UUID userId);

    void sendFriendRequest(String authHeader, FriendIdRequest request);
}
