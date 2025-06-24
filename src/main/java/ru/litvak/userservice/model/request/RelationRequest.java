package ru.litvak.userservice.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RelationRequest {
    @NotNull
    private UUID me;
    @NotNull
    private UUID friend;
}
