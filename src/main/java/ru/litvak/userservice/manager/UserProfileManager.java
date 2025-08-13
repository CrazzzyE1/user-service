package ru.litvak.userservice.manager;

import jakarta.validation.constraints.NotNull;
import ru.litvak.userservice.enumerated.StatusType;
import ru.litvak.userservice.model.dto.UserProfileDto;
import ru.litvak.userservice.model.entity.ShortUserProfile;
import ru.litvak.userservice.model.entity.UserProfile;
import ru.litvak.userservice.model.response.LocalizedEnum;
import ru.litvak.userservice.model.response.RelationResponse;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public interface UserProfileManager {

    void update(UserProfileDto profileDto);

    UserProfile get(UUID me, UUID id);

    List<LocalizedEnum> getUserStatuses(Class<? extends Enum<?>> enumClass, Locale locale);

    void updateUserStatus(UUID me, StatusType status);

    RelationResponse getRelations(@NotNull UUID me, @NotNull UUID friend);

    UserProfile edit(UUID me, UserProfileDto userProfileDto);

    void delete(UUID me);

    Boolean isActive(UUID id);

    ShortUserProfile getShortProfile(UUID id);

    List<UserProfile> search(String query);
}
