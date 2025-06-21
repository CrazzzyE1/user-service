package ru.litvak.userservice.manager;

import ru.litvak.userservice.enumerated.StatusType;
import ru.litvak.userservice.model.dto.UserProfileDto;
import ru.litvak.userservice.model.entity.UserProfile;
import ru.litvak.userservice.model.response.LocalizedEnum;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public interface UserProfileManager {

    void update(UserProfileDto profileDto);

    UserProfile getUserProfile(UUID me, UUID id);

    List<LocalizedEnum> getUserStatuses(Class<? extends Enum<?>> enumClass, Locale locale);

    void updateUserStatus(UUID me, StatusType status);
}
