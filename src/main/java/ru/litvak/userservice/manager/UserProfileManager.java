package ru.litvak.userservice.manager;

import ru.litvak.userservice.model.dto.UserProfileDto;
import ru.litvak.userservice.model.entity.UserProfile;

import java.util.UUID;

public interface UserProfileManager {

    void update(UserProfileDto profileDto);

    UserProfile getUserProfile(UUID me, UUID id);
}
