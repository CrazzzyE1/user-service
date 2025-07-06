package ru.litvak.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.litvak.userservice.model.request.FriendIdRequest;
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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/requests")
    public void sendFriendRequest(@RequestHeader(value = "Authorization") String authHeader,
                                  @RequestBody FriendIdRequest request) {
        friendService.sendFriendRequest(authHeader, request);
    }
//    подтверждения заявки (PUT /friends/requests/{requestId}/accept)
//
//    отклонения заявки (DELETE /friends/requests/{requestId})

}
