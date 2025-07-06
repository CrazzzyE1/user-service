package ru.litvak.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.litvak.userservice.manager.FriendManager;
import ru.litvak.userservice.model.request.FriendIdRequest;
import ru.litvak.userservice.model.response.GetFriendsResponse;
import ru.litvak.userservice.service.FriendService;
import ru.litvak.userservice.util.JwtTokenMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendManager friendManager;

    @Override
    public GetFriendsResponse getFriends(String authHeader, UUID userId) {
        UUID me = JwtTokenMapper.map(authHeader).getId();
        return friendManager.getFriends(me, userId);
    }

    @Override
    public void sendFriendRequest(String authHeader, FriendIdRequest request) {
        UUID me = JwtTokenMapper.map(authHeader).getId();
        friendManager.createFriendRequest(me, request.getFriendId());
    }
}
