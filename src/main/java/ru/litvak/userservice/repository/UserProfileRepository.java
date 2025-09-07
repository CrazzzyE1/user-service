package ru.litvak.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.litvak.userservice.model.entity.ShortUserProfile;
import ru.litvak.userservice.model.entity.UserProfile;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID>, JpaSpecificationExecutor<UserProfile> {

    Optional<ShortUserProfile> findShortById(UUID id);

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO user_profiles (id, username, full_name, first_name, family_name, email, is_email_verified, birth_date)
                   VALUES (:#{#profile.id}, :#{#profile.username}, :#{#profile.fullName}, :#{#profile.firstName}, 
                    :#{#profile.familyName}, :#{#profile.email}, :#{#profile.isEmailVerified}, :#{#profile.birthDate}) 
                                ON CONFLICT (id) DO NOTHING
            """, nativeQuery = true)
    void insertIgnore(@Param("profile") UserProfile profile);

}
