package ru.litvak.userservice.manager;

import ru.litvak.userservice.model.response.GetFriendsResponse;

import java.util.UUID;

public interface FriendManager {
    GetFriendsResponse getFriends(UUID me, UUID userId);

    void delete(UUID me, UUID id);
}
