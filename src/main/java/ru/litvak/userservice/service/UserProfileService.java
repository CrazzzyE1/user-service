package ru.litvak.userservice.service;

import ru.litvak.userservice.model.dto.UserProfileDto;
import ru.litvak.userservice.model.response.LocalizedEnum;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public interface UserProfileService {
    UserProfileDto getMe(String authHeader);

    UserProfileDto getUserProfile(String authHeader, UUID id);

    List<LocalizedEnum> getUserStatuses(Class<? extends Enum<?>> enumClass, Locale locale);
}
