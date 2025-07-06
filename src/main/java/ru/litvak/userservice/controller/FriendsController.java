package ru.litvak.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.litvak.userservice.model.response.GetFriendsResponse;
import ru.litvak.userservice.service.FriendService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendsController {

    private final FriendService friendService;

    @GetMapping("/user/{userId}")
    public GetFriendsResponse getFriends(@RequestHeader(value = "Authorization") String authHeader,
                                         @PathVariable UUID userId) {
        return friendService.getFriends(authHeader, userId);
    }
}
