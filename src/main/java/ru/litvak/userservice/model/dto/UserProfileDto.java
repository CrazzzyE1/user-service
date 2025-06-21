package ru.litvak.userservice.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.litvak.userservice.enumerated.DeleteReason;
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
    private String email;
    private Boolean isEmailVerified;
    private LocalDate birthDate;
    private Integer friendsCount;

    private StatusType status;
    private Boolean isOwner;
    private String location;
    private String gender;
    private Boolean isPublic;
    private PrivacyLevel privacyLevel;
    private String language;
    private Instant createdAt;
    private Instant updatedAt;
    private Boolean isDeleted;
    private Instant deletedAt;
    private DeleteReason deletionReason;

}
