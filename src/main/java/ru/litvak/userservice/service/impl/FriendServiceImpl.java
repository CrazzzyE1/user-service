package ru.litvak.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.litvak.userservice.manager.FriendManager;
import ru.litvak.userservice.manager.FriendRequestManager;
import ru.litvak.userservice.mapper.FriendRequestMapper;
import ru.litvak.userservice.model.dto.FriendRequestDto;
import ru.litvak.userservice.model.entity.FriendRequest;
import ru.litvak.userservice.model.request.FriendIdRequest;
import ru.litvak.userservice.model.response.GetFriendsResponse;
import ru.litvak.userservice.service.FriendService;
import ru.litvak.userservice.util.JwtTokenMapper;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendManager friendManager;
    private final FriendRequestManager friendRequestManager;
    private final FriendRequestMapper friendRequestMapper;

    @Override
    public GetFriendsResponse getFriends(String authHeader, UUID userId) {
        UUID me = JwtTokenMapper.map(authHeader).getId();
        return friendManager.getFriends(me, userId);
    }

    @Override
    public void sendFriendRequest(String authHeader, FriendIdRequest request) {
        UUID me = JwtTokenMapper.map(authHeader).getId();
        friendRequestManager.create(me, request.getFriendId());
    }

    @Override
    public List<FriendRequestDto> getFriendRequest(String authHeader, boolean incoming) {
        UUID me = JwtTokenMapper.map(authHeader).getId();
        List<FriendRequest> requests = friendRequestManager.get(me, incoming);
        return friendRequestMapper.toListDto(requests);
    }

    @Override
    public void acceptFriendRequest(String authHeader, Long requestId) {
        UUID me = JwtTokenMapper.map(authHeader).getId();
        friendRequestManager.accept(me, requestId);
    }
}
