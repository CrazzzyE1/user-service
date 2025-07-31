package ru.litvak.userservice.event;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
public class EventDto {

    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Builder.Default
    private Instant created = Instant.now();

    private UUID senderId;
    private UUID recipientId;
    private EventType type;
    private String entityId;
    private List<NotificationMethod> methods;
}
