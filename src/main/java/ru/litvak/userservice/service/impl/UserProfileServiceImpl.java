package ru.litvak.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.litvak.userservice.enumerated.StatusType;
import ru.litvak.userservice.manager.UserProfileManager;
import ru.litvak.userservice.mapper.UserProfileMapper;
import ru.litvak.userservice.model.dto.ShortUserProfileDto;
import ru.litvak.userservice.model.dto.UserProfileDto;
import ru.litvak.userservice.model.request.RelationRequest;
import ru.litvak.userservice.model.response.LocalizedEnum;
import ru.litvak.userservice.model.response.RelationResponse;
import ru.litvak.userservice.service.UserProfileService;
import ru.litvak.userservice.util.JwtTokenMapper;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileManager userProfileManager;
    private final UserProfileMapper userProfileMapper;

    @Override
    public UserProfileDto getMe(String authHeader) {
        UUID me = JwtTokenMapper.map(authHeader).getId();
        return userProfileMapper.toDto(userProfileManager.get(me, me));
    }

    @Override
    public UserProfileDto getUserProfile(String authHeader, UUID id) {
        UUID me = JwtTokenMapper.map(authHeader).getId();
        return userProfileMapper.toDto(userProfileManager.get(me, id));
    }

    @Override
    public List<LocalizedEnum> getUserStatuses(Class<? extends Enum<?>> enumClass, Locale locale) {
        return userProfileManager.getUserStatuses(enumClass, locale);
    }

    @Override
    public void updateUserStatus(String authHeader, StatusType status) {
        UUID me = JwtTokenMapper.map(authHeader).getId();
        userProfileManager.updateUserStatus(me, status);
    }

    @Override
    public RelationResponse getRelations(RelationRequest request) {
        return userProfileManager.getRelations(request.getMe(), request.getFriend());
    }

    @Override
    public UserProfileDto editUserProfile(String authHeader, UserProfileDto userProfileDto) {
        UUID me = JwtTokenMapper.map(authHeader).getId();
        return userProfileMapper.toDto(userProfileManager.edit(me, userProfileDto));
    }

    @Override
    public void deleteUserProfile(String authHeader) {
        UUID me = JwtTokenMapper.map(authHeader).getId();
        userProfileManager.delete(me);
    }

    @Override
    public Boolean isProfileActive(UUID id) {
        return userProfileManager.isActive(id);
    }

    @Override
    public ShortUserProfileDto getShortUserProfile(UUID id) {
        return userProfileMapper.toShortDto(userProfileManager.getShortProfile(id));
    }
}
