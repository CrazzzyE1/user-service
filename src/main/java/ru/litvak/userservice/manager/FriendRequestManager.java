package ru.litvak.userservice.manager;

import ru.litvak.userservice.model.entity.FriendRequest;

import java.util.List;
import java.util.UUID;

public interface FriendRequestManager {
    void accept(UUID me, Long id);

    void create(UUID me, UUID friendId);

    List<FriendRequest> get(UUID me, boolean incoming);

    void delete(UUID me, Long id, Boolean isCanceled);
}
