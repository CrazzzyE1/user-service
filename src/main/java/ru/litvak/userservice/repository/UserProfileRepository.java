package ru.litvak.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.litvak.userservice.model.entity.ShortUserProfile;
import ru.litvak.userservice.model.entity.UserProfile;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID>, JpaSpecificationExecutor<UserProfile> {

    Optional<ShortUserProfile> findShortById(UUID id);
}
