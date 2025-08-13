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
            "WHERE LOWER(up.fullName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(up.email) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "AND up.isDeleted = false " +
            "ORDER BY " +
            "  CASE " +
            "    WHEN LOWER(up.fullName) = LOWER(:query) THEN 0 " +
            "    WHEN LOWER(up.email) = LOWER(:query) THEN 1 " +
            "    WHEN LOWER(up.fullName) LIKE LOWER(CONCAT(:query, '%')) THEN 2 " +
            "    WHEN LOWER(up.email) LIKE LOWER(CONCAT(:query, '%')) THEN 3 " +
            "    ELSE 4 " +
            "  END")
    List<UserProfile> searchAllByQuery(@Param("query") String query);
}
