package ru.litvak.userservice.service;

public interface KafkaEventPublisher {

    <T> void publish(String topic, String key, T payload, Class<?> eventClass);
}
