package ru.litvak.userservice.event;

import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequestEvent {
    private Long requestId;
    private UUID me;
    private UUID friend;
}
