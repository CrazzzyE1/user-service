package ru.litvak.userservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.litvak.userservice.model.dto.UserProfileDto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

public class JwtTokenMapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static UserProfileDto map(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing or invalid Authorization header");
        }
        String jwtToken = authHeader.replace("Bearer ", "");
        String[] parts = jwtToken.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid JWT token format");
        }

        String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
        Map<String, Object> claims;
        try {
            claims = new ObjectMapper().readValue(payloadJson, Map.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid readValue for Claims");
        }

        return UserProfileDto.builder()
                .id(UUID.fromString((String) claims.get("sub")))
                .username((String) claims.get("preferred_username"))
                .fullName((String) claims.get("name"))
                .firstName((String) claims.get("given_name"))
                .familyName((String) claims.get("family_name"))
                .email((String) claims.get("email"))
                .isEmailVerified((Boolean) claims.get("email_verified"))
                .birthDate(LocalDate.parse((String) claims.get("birthdate"), formatter))
                .build();
    }
}
