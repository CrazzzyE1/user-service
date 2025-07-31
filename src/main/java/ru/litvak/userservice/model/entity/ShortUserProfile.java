package ru.litvak.userservice.model.entity;

import java.util.UUID;

public interface ShortUserProfile {
    UUID getId();
    String getFullName();
    String getLocation();
    Boolean getIsDeleted();
    String getEmail();
}
