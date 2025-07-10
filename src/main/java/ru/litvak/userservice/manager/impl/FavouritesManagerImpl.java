package ru.litvak.userservice.manager.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.litvak.userservice.exception.NotFoundException;
import ru.litvak.userservice.exception.RequestParameterException;
import ru.litvak.userservice.manager.FavouritesManager;
import ru.litvak.userservice.model.entity.UserProfile;
import ru.litvak.userservice.model.response.GetFavouritesResponse;
import ru.litvak.userservice.repository.UserProfileRepository;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FavouritesManagerImpl implements FavouritesManager {

    private final UserProfileRepository userProfileRepository;

    @Override
    public GetFavouritesResponse get(UUID me) {
        UserProfile userProfile = userProfileRepository.findById(me)
                .orElseThrow(() -> new NotFoundException("User profile with id %s not found.".formatted(me)));
        return new GetFavouritesResponse((userProfile.getFavourites().stream()
                .map(UserProfile::getId)
                .toList()));
    }

    @Override
    @Transactional
    public void add(UUID me, UUID userId) {
        UserProfile meProfile = userProfileRepository.findById(me)
                .orElseThrow(() -> new NotFoundException("User profile with id %s not found.".formatted(me)));
        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User profile with id %s not found.".formatted(userId)));
        if (!userProfile.isPublic()) {
            throw new RequestParameterException("friendId", "User not Public");
        }

        if (userProfile.getFriends().stream()
                .map(UserProfile::getId)
                .toList()
                .contains(me)) {
            throw new RequestParameterException("friendId", "Users are already friends");
        }

        meProfile.getFavourites().add(userProfile);
    }

    @Override
    @Transactional
    public void delete(UUID me, UUID userId) {
        UserProfile meProfile = userProfileRepository.findById(me)
                .orElseThrow(() -> new NotFoundException("User profile with id %s not found.".formatted(me)));
        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User profile with id %s not found.".formatted(userId)));
        meProfile.getFavourites().remove(userProfile);
    }
}
