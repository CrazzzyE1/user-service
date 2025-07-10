package ru.litvak.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.litvak.userservice.model.dto.FriendRequestDto;
import ru.litvak.userservice.model.request.FriendIdRequest;
import ru.litvak.userservice.model.response.GetFriendsResponse;
import ru.litvak.userservice.service.FriendService;

import java.util.List;
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
                                  @RequestBody @Valid FriendIdRequest request) {
        friendService.sendFriendRequest(authHeader, request);
    }

    @GetMapping("/requests")
    public List<FriendRequestDto> getFriendRequest(@RequestHeader(value = "Authorization") String authHeader,
                                                   @RequestParam(required = false, defaultValue = "true") boolean incoming) {
        return friendService.getFriendRequest(authHeader, incoming);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/requests/{requestId}/accept")
    public void acceptFriendRequest(@RequestHeader(value = "Authorization") String authHeader,
                                    @PathVariable Long requestId) {
        friendService.acceptFriendRequest(authHeader, requestId);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/requests/{requestId}")
    public void deleteFriendRequest(@RequestHeader(value = "Authorization") String authHeader,
                                    @PathVariable Long requestId,
                                    @RequestParam Boolean isCanceled) {
        friendService.deleteFriendRequest(authHeader, requestId, isCanceled);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteFriend(@RequestHeader(value = "Authorization") String authHeader,
                             @PathVariable UUID id) {
        friendService.deleteFriend(authHeader, id);
    }
}
