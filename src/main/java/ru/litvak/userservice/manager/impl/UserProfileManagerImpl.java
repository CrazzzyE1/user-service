package ru.litvak.userservice.manager.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.litvak.userservice.enumerated.StatusType;
import ru.litvak.userservice.exception.NotFoundException;
import ru.litvak.userservice.manager.UserProfileManager;
import ru.litvak.userservice.model.dto.UserProfileDto;
import ru.litvak.userservice.model.entity.EnumLocalization;
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

    private final UserProfileRepository userProfileRepository;
    private final EnumLocalizationRepository enumLocalizationRepository;

    @Transactional
    @Override
    public void update(UserProfileDto profileDto) {
        Optional<UserProfile> profileOptional = userProfileRepository.findById(profileDto.getId());
        UserProfile userProfile;
        if (profileOptional.isPresent()) {
            userProfile = profileOptional.get();
        } else {
            userProfile = new UserProfile();
            userProfile.setId(profileDto.getId());
        }
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
        UserProfile userProfile = userProfileRepository.findById(friend)
                .orElseThrow(() -> new NotFoundException("User profile with id %s not found.".formatted(friend)));
        return new RelationResponse(userProfile.getPrivacyLevel(), isFriends(me, userProfile));
    }

    @Override
    @Transactional
    public UserProfile edit(UUID me, UserProfileDto userProfileDto) {
        UserProfile toEdit = userProfileRepository.findById(me)
                .orElseThrow(() -> new NotFoundException("User profile with id %s not found.".formatted(me)));
        if (toEdit.getIsDeleted()) {
            throw new NotFoundException("User profile with id %s not found.".formatted(me));
        }
        toEdit.setPrivacyLevel(userProfileDto.getPrivacyLevel());
        toEdit.setStatus(userProfileDto.getStatus());
        toEdit.setGender(userProfileDto.getGender());
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
    public List<UserProfile> search(String query) {
        return userProfileRepository.searchAllByQuery(query);
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
}
