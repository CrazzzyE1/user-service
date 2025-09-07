package ru.litvak.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.litvak.userservice.model.entity.ShortUserProfile;
import ru.litvak.userservice.model.entity.UserProfile;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID>, JpaSpecificationExecutor<UserProfile> {

    Optional<ShortUserProfile> findShortById(UUID id);

    @Modifying
    @Query(value = """
        INSERT INTO user_profiles (id, username, full_name, first_name, family_name, 
                                  email, is_email_verified, birth_date, attempts_change_birth_date, privacy_level, 
                                           created_at)
        VALUES (:id, :username, :fullName, :firstName, :familyName, :email, :isEmailVerified, :birthDate, 0, 'PUBLIC', 
                        CURRENT_TIMESTAMP)
        ON CONFLICT (id) DO NOTHING
        """, nativeQuery = true)
    void insertIfNotExists(@Param("id") UUID id,
                           @Param("username") String username,
                           @Param("fullName") String fullName,
                           @Param("firstName") String firstName,
                           @Param("familyName") String familyName,
                           @Param("email") String email,
                           @Param("isEmailVerified") Boolean isEmailVerified,
                           @Param("birthDate") LocalDate birthDate);

}
