package ru.litvak.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.litvak.userservice.enumerated.StatusType;
import ru.litvak.userservice.model.dto.UserProfileDto;
import ru.litvak.userservice.model.request.RelationRequest;
import ru.litvak.userservice.model.request.UpdateUserStatusRequest;
import ru.litvak.userservice.model.response.LocalizedEnum;
import ru.litvak.userservice.model.response.RelationResponse;
import ru.litvak.userservice.service.UserProfileService;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profiles")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/me")
    public UserProfileDto getOwnerUserProfile(@RequestHeader(value = "Authorization") String authHeader) {
        return userProfileService.getMe(authHeader);
    }

    @GetMapping("/{id}")
    public UserProfileDto getUserProfile(@RequestHeader(value = "Authorization") String authHeader,
                                         @PathVariable UUID id) {
        return userProfileService.getUserProfile(authHeader, id);
    }

    @GetMapping("/statuses")
    public List<LocalizedEnum> getUserStatuses(Locale locale) {
        return userProfileService.getUserStatuses(StatusType.class, locale);
    }

    @ResponseStatus(NO_CONTENT)
    @PatchMapping("/statuses")
    public void updateUserStatus(@RequestHeader(value = "Authorization") String authHeader,
                                 @RequestBody @Valid UpdateUserStatusRequest request) {
        userProfileService.updateUserStatus(authHeader, request.getStatus());
    }

    @PostMapping("/relations")
    public RelationResponse getRelations(@RequestBody @Valid RelationRequest request) {
        return userProfileService.getRelations(request);
    }

    @PatchMapping()
    public UserProfileDto editUserProfile(@RequestHeader(value = "Authorization") String authHeader,
                                          @RequestBody @Valid UserProfileDto userProfileDto) {
        return userProfileService.editUserProfile(authHeader, userProfileDto);
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping()
    public void deleteUserProfile(@RequestHeader(value = "Authorization") String authHeader) {
        userProfileService.deleteUserProfile(authHeader);
    }

    @GetMapping("/{id}/active")
    public Boolean isProfileActive(@PathVariable UUID id) {
        return userProfileService.isProfileActive(id);
    }
}
