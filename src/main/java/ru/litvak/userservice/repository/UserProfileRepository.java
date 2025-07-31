package ru.litvak.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.litvak.userservice.model.entity.ShortUserProfile;
import ru.litvak.userservice.model.entity.UserProfile;

import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    Optional<ShortUserProfile> findShortById(UUID id);
}
