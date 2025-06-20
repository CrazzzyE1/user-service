package ru.litvak.userservice.manager.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.litvak.userservice.manager.UserProfileManager;
import ru.litvak.userservice.model.dto.UserProfileDto;
import ru.litvak.userservice.model.entity.UserProfile;
import ru.litvak.userservice.repository.UserProfileRepository;

import java.util.Optional;
import java.util.UUID;

import static ru.litvak.userservice.enumerated.PrivacyLevel.*;

@Component
@RequiredArgsConstructor
public class UserProfileManagerImpl implements UserProfileManager {

    private final UserProfileRepository userProfileRepository;

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
    public UserProfile getUserProfile(UUID me, UUID id) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User profile with id %s not found.".formatted(id)));
        boolean isOwner = me.equals(id);
        if (isOwner || userProfile.isPublic() || isFriends(me, userProfile)) {
            userProfile.setIsOwner(isOwner);
            return userProfile;
        }

        return createDummyUserProfileWithoutPrivateFields(userProfile);
    }

    private UserProfile createDummyUserProfileWithoutPrivateFields(UserProfile userProfile) {
        UserProfile profile = new UserProfile();
        profile.setId(userProfile.getId());
        profile.setFullName(userProfile.getFullName());
        profile.setFirstName(userProfile.getFirstName());
        profile.setFamilyName(userProfile.getFamilyName());
        profile.setPrivacyLevel(userProfile.getPrivacyLevel());
        return profile;
    }

    private boolean isFriends(UUID me, UserProfile userProfile) {
        if (FRIENDS_ONLY.equals(userProfile.getPrivacyLevel())) {
            return userProfile.getFriends().stream()
                    .map(UserProfile::getId)
                    .anyMatch(id -> id.equals(me));
        }
        return false;
    }
}
