package ru.litvak.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.litvak.userservice.model.dto.UserProfileDto;
import ru.litvak.userservice.service.UserProfileService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserProfileService userProfileService;

    @GetMapping("/me")
    public UserProfileDto getUsers(@RequestHeader(value = "Authorization") String authHeader) {
        return userProfileService.getMe(authHeader);
    }

    @GetMapping("/{id}")
    public UserProfileDto getUserProfile(@RequestHeader(value = "Authorization") String authHeader,
                                         @PathVariable UUID id) {
        return userProfileService.getUserProfile(authHeader, id);
    }
}
