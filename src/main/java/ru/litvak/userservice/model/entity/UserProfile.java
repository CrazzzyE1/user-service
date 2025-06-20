package ru.litvak.userservice.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.litvak.userservice.enumerated.DeleteReason;
import ru.litvak.userservice.enumerated.PrivacyLevel;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "user_profiles"   )
public class UserProfile {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    private String fullName;
    private String firstName;
    private String familyName;

    @Column(nullable = false, unique = true)
    private String email;

    private Boolean isEmailVerified;
    private LocalDate birthDate;
    private String avatar;
    private String location;
    private String gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrivacyLevel privacyLevel = PrivacyLevel.PUBLIC;

    private String language;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    private Boolean isDeleted = false;
    private Instant deletedAt;

    @Enumerated(EnumType.STRING)
    private DeleteReason deletionReason;

    @ManyToMany
    @JoinTable(
            name = "user_profile_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private Set<UserProfile> friends = new HashSet<>();

    @Transient
    private Boolean isOwner;

    public Boolean isPublic() {
        return PrivacyLevel.PUBLIC.equals(privacyLevel);
    }

}
