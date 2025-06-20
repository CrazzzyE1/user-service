package ru.litvak.userservice.service;

import ru.litvak.userservice.model.dto.UserProfileDto;

import java.util.UUID;

public interface UserProfileService {
    UserProfileDto getMe(String authHeader);

    UserProfileDto getUserProfile(String authHeader, UUID id);
}
