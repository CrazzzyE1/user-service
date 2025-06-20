package ru.litvak.userservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface AuthorizeService {
    void authorize(String authHeader) throws JsonProcessingException;
}
