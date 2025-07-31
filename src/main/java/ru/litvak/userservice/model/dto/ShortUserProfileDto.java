package ru.litvak.userservice.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class ShortUserProfileDto {
    private UUID id;
    private String fullName;
    private String location;
    private Boolean isDeleted;
    private String email;
}
