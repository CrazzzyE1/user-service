package ru.litvak.userservice.manager.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.litvak.userservice.manager.FriendManager;
import ru.litvak.userservice.manager.UserProfileManager;
import ru.litvak.userservice.model.entity.UserProfile;
import ru.litvak.userservice.model.response.GetFriendsResponse;
import ru.litvak.userservice.repository.UserProfileRepository;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FriendManagerImpl implements FriendManager {

    private final UserProfileManager userProfileManager;
    private final UserProfileRepository userProfileRepository;

    @Override
    public GetFriendsResponse getFriends(UUID me, UUID userId) {
        UserProfile userProfile = userProfileManager.getUserProfile(me, userId);
        return new GetFriendsResponse(userProfile.getFriends().stream()
                .map(UserProfile::getId)
                .toList());
    }
}
