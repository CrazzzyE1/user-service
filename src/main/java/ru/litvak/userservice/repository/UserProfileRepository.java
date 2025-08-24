package ru.litvak.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.litvak.userservice.model.entity.ShortUserProfile;
import ru.litvak.userservice.model.entity.UserProfile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    Optional<ShortUserProfile> findShortById(UUID id);

    @Query("SELECT up FROM UserProfile up " +
            "WHERE up.id != :me " +
            "AND up.isDeleted = false " +
            "AND (" +
            "   LOWER(up.fullName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "   OR LOWER(up.firstName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "   OR LOWER(up.familyName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            ") " +
            "ORDER BY " +
            "  CASE " +
            "    WHEN LOWER(up.fullName) = LOWER(:query) THEN 0 " +
            "    WHEN LOWER(up.firstName) = LOWER(:query) THEN 1 " +
            "    WHEN LOWER(up.familyName) = LOWER(:query) THEN 2 " +
            "    WHEN LOWER(up.fullName) LIKE LOWER(CONCAT(:query, '%')) THEN 3 " +
            "    WHEN LOWER(up.firstName) LIKE LOWER(CONCAT(:query, '%')) THEN 4 " +
            "    WHEN LOWER(up.familyName) LIKE LOWER(CONCAT(:query, '%')) THEN 5 " +
            "    ELSE 6 " +
            "  END")
    List<UserProfile> searchAllByQuery(@Param("query") String query, @Param("me") UUID me);

}
