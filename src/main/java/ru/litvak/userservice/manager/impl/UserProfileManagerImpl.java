package ru.litvak.userservice.manager.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.litvak.userservice.enumerated.StatusType;
import ru.litvak.userservice.exception.NotFoundException;
import ru.litvak.userservice.manager.FriendRequestManager;
import ru.litvak.userservice.manager.UserProfileManager;
import ru.litvak.userservice.model.dto.UserProfileDto;
import ru.litvak.userservice.model.entity.EnumLocalization;
import ru.litvak.userservice.model.entity.FriendRequest;
import ru.litvak.userservice.model.entity.ShortUserProfile;
import ru.litvak.userservice.model.entity.UserProfile;
import ru.litvak.userservice.model.response.LocalizedEnum;
import ru.litvak.userservice.model.response.RelationResponse;
import ru.litvak.userservice.repository.EnumLocalizationRepository;
import ru.litvak.userservice.repository.UserProfileRepository;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Boolean.TRUE;
import static ru.litvak.userservice.enumerated.DeleteReason.USER_REQUEST;
import static ru.litvak.userservice.enumerated.PrivacyLevel.FRIENDS_ONLY;

@Component
@RequiredArgsConstructor
public class UserProfileManagerImpl implements UserProfileManager {

    private final FriendRequestManager friendRequestManager;
    private final UserProfileRepository userProfileRepository;
    private final EnumLocalizationRepository enumLocalizationRepository;

    @Transactional
    @Override
    public void update(UserProfileDto profileDto) {
        Optional<UserProfile> profileOptional = userProfileRepository.findById(profileDto.getId());

        if (profileOptional.isPresent()) {
            return;
        }

        UserProfile userProfile = new UserProfile();
        userProfile.setId(profileDto.getId());
        userProfile.setUsername(profileDto.getUsername());
        userProfile.setFullName(profileDto.getFullName());
        userProfile.setFirstName(profileDto.getFirstName());
        userProfile.setFamilyName(profileDto.getFamilyName());
        userProfile.setEmail(profileDto.getEmail());
        userProfile.setIsEmailVerified(profileDto.getIsEmailVerified());
        userProfile.setBirthDate(profileDto.getBirthDate());
        userProfileRepository.save(userProfile);
    }

    @Transactional
    @Override
    public UserProfile get(UUID me, UUID id) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User profile with id %s not found.".formatted(id)));
        boolean isOwner = me.equals(id);
        if (!isOwner) {
            userProfile.setIsFriend(isFriends(me, userProfile));
        }
        if ((isOwner
                || userProfile.isPublic()
                || FRIENDS_ONLY.equals(userProfile.getPrivacyLevel()) && isFriends(me, userProfile))
                && !userProfile.getIsDeleted()) {
            userProfile.setIsOwner(isOwner);
            return userProfile;
        }
        return createDummyUserProfileWithoutPrivateFields(userProfile);
    }

    @Override
    public List<LocalizedEnum> getUserStatuses(Class<? extends Enum<?>> enumClass, Locale locale) {
        String enumType = enumClass.getSimpleName();
        List<EnumLocalization> localizations = enumLocalizationRepository.findByEnumTypeAndLocale(enumType, locale.getLanguage());

        Map<String, String> labelMap = localizations.stream()
                .collect(Collectors.toMap(EnumLocalization::getEnumValue, EnumLocalization::getLabel));

        return Arrays.stream(enumClass.getEnumConstants())
                .map(e -> new LocalizedEnum(e.name(), labelMap.getOrDefault(e.name(), e.name())))
                .toList();
    }

    @Transactional
    @Override
    public void updateUserStatus(UUID me, StatusType status) {
        UserProfile userProfile = userProfileRepository.findById(me)
                .orElseThrow(() -> new NotFoundException("User profile with id %s not found.".formatted(me)));
        userProfile.setStatus(status);
    }

    @Override
    public RelationResponse getRelations(UUID me, UUID friend) {
        UserProfile friendProfile = userProfileRepository.findById(friend)
                .orElseThrow(() -> new NotFoundException("User profile with id %s not found.".formatted(friend)));

        UserProfile profile = userProfileRepository.findById(me)
                .orElseThrow(() -> new NotFoundException("User profile with id %s not found.".formatted(me)));

        return RelationResponse.builder()
                .privacyLevel(friendProfile.getPrivacyLevel())
                .isFriends(isFriends(me, friendProfile))
                .isFavourites(isFavourites(friend, profile))
                .hasIncomeFriendsRequest(hasIncomeFriendsRequest(me, friend))
                .hasOutcomeFriendsRequest(hasOutcomeFriendsRequest(me, friend))
                .build();
    }

    @Override
    @Transactional
    public UserProfile edit(UUID me, UserProfile newData) {
        UserProfile toEdit = userProfileRepository.findById(me)
                .orElseThrow(() -> new NotFoundException("User profile with id %s not found.".formatted(me)));
        if (toEdit.getIsDeleted()) {
            throw new NotFoundException("User profile with id %s not found.".formatted(me));
        }

        toEdit.setFirstName(newData.getFirstName());
        toEdit.setFamilyName(newData.getFamilyName());
        toEdit.setFullName(newData.getFullName());
        toEdit.setBirthDate(newData.getBirthDate());
        toEdit.setPrivacyLevel(newData.getPrivacyLevel());
        toEdit.setStatus(newData.getStatus());
        toEdit.setGender(newData.getGender());
        return toEdit;
    }

    @Override
    @Transactional
    public void delete(UUID me) {
        UserProfile toDelete = userProfileRepository.findById(me)
                .orElseThrow(() -> new NotFoundException("User profile with id %s not found.".formatted(me)));
        toDelete.setIsDeleted(TRUE);
        toDelete.setDeletedAt(Instant.now());
        toDelete.setDeletionReason(USER_REQUEST);
    }

    @Override
    public Boolean isActive(UUID id) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User profile with id %s not found.".formatted(id)));
        return !TRUE.equals(userProfile.getIsDeleted());
    }

    @Override
    public ShortUserProfile getShortProfile(UUID id) {
        return userProfileRepository.findShortById(id)
                .orElseThrow(() -> new NotFoundException("User profile with id %s not found.".formatted(id)));
    }

    @Override
    public List<UserProfile> search(String query, UUID me) {
        return userProfileRepository.searchAllByQuery(query, me);
    }


    private UserProfile createDummyUserProfileWithoutPrivateFields(UserProfile userProfile) {
        UserProfile profile = new UserProfile();
        profile.setId(userProfile.getId());
        profile.setFullName(userProfile.getFullName());
        profile.setFirstName(userProfile.getFirstName());
        profile.setFamilyName(userProfile.getFamilyName());
        profile.setPrivacyLevel(userProfile.getPrivacyLevel());
        profile.setIsDeleted(userProfile.getIsDeleted());
        return profile;
    }

    private boolean isFriends(UUID me, UserProfile userProfile) {
        return userProfile.getFriends().stream()
                .map(UserProfile::getId)
                .anyMatch(id -> id.equals(me));
    }

    private boolean isFavourites(UUID friend, UserProfile userProfile) {
        return userProfile.getFavourites().stream()
                .map(UserProfile::getId)
                .anyMatch(id -> id.equals(friend));
    }

    private boolean hasIncomeFriendsRequest(UUID me, UUID friend) {
        return friendRequestManager.get(me, true).stream()
                .map(FriendRequest::getSender)
                .anyMatch(sender -> sender.getId().equals(friend));
    }


    private boolean hasOutcomeFriendsRequest(UUID me, UUID friend) {
        return friendRequestManager.get(me, false).stream()
                .map(FriendRequest::getReceiver)
                .anyMatch(receiver -> receiver.getId().equals(friend));
    }
}
