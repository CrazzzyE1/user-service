package ru.litvak.userservice.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FriendIdRequest {
    private UUID friendId;
}
