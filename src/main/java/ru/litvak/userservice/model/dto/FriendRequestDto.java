package ru.litvak.userservice.model.dto;

import lombok.Getter;
import lombok.Setter;
import ru.litvak.userservice.enumerated.FriendRequestStatus;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class FriendRequestDto {
    private Long id;
    private UUID sender;
    private UUID receiver;
    private FriendRequestStatus status;
    private Instant createdAt;
}
