package ru.litvak.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import ru.litvak.userservice.service.KafkaEventPublisher;

@Service
@RequiredArgsConstructor
public class KafkaEventPublisherImpl implements KafkaEventPublisher {

    @Value("${notifications.json.trusted.package.name}")
    private String trustedPackage;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public <T> void publish(String topic, String key, T payload, Class<?> eventClass) {
        Message<T> message = MessageBuilder
                .withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader(KafkaHeaders.KEY, key)
                .setHeader("__TypeId__", trustedPackage + eventClass.getSimpleName())
                .build();
        kafkaTemplate.send(message);
    }
}
