package ru.litvak.userservice.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.litvak.userservice.enumerated.DeleteReason;
import ru.litvak.userservice.enumerated.Gender;
import ru.litvak.userservice.enumerated.PrivacyLevel;
import ru.litvak.userservice.enumerated.StatusType;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
@Setter
public class UserProfileDto {
    private UUID id;
    private String username;
    private String fullName;
    private String firstName;
    private String familyName;
    private Gender gender;
    private String email;
    private Boolean isEmailVerified;
    private LocalDate birthDate;
    private Integer friendsCount;
    private Integer favouritesCount;

    private StatusType status;
    private Boolean isOwner;
    private Boolean isFriend;
    private String location;
    private Boolean isPublic;

    @NotNull
    private PrivacyLevel privacyLevel;
    private String language;
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean isDeleted;
    private Instant deletedAt;
    private DeleteReason deletionReason;

}
