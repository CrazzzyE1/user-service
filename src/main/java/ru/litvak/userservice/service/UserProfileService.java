package ru.litvak.userservice.service;

import jakarta.validation.Valid;
import ru.litvak.userservice.enumerated.StatusType;
import ru.litvak.userservice.model.dto.ShortUserProfileDto;
import ru.litvak.userservice.model.dto.UserProfileDto;
import ru.litvak.userservice.model.request.RelationRequest;
import ru.litvak.userservice.model.response.LocalizedEnum;
import ru.litvak.userservice.model.response.RelationResponse;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public interface UserProfileService {
    UserProfileDto getMe(String authHeader);

    UserProfileDto getUserProfile(String authHeader, UUID id);

    List<LocalizedEnum> getUserStatuses(Class<? extends Enum<?>> enumClass, Locale locale);

    void updateUserStatus(String authHeader, StatusType status);

    RelationResponse getRelations(@Valid RelationRequest request);

    UserProfileDto editUserProfile(String authHeader, @Valid UserProfileDto userProfileDto);

    void deleteUserProfile(String authHeader);

    Boolean isProfileActive(UUID id);

    ShortUserProfileDto getShortUserProfile(UUID id);

    List<UserProfileDto> search(String query);
}
