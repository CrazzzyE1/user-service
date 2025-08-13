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
            "   OR (" +
            "       LOWER(SUBSTRING(up.email, 1, LOCATE('@', up.email) - 1)) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "       AND up.email LIKE '%@%'" +  // Гарантируем, что email содержит @
            "   )" +
            ") " +
            "ORDER BY " +
            "  CASE " +
            "    WHEN LOWER(up.fullName) = LOWER(:query) THEN 0 " +
            "    WHEN LOWER(SUBSTRING(up.email, 1, LOCATE('@', up.email) - 1)) = LOWER(:query) THEN 1 " +
            "    WHEN LOWER(up.fullName) LIKE LOWER(CONCAT(:query, '%')) THEN 2 " +
            "    WHEN LOWER(SUBSTRING(up.email, 1, LOCATE('@', up.email) - 1)) LIKE LOWER(CONCAT(:query, '%')) THEN 3 " +
            "    ELSE 4 " +
            "  END")
    List<UserProfile> searchAllByQuery(@Param("query") String query, @Param("me") UUID me);
}
