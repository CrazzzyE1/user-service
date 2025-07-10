package ru.litvak.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.litvak.userservice.model.request.FriendIdRequest;
import ru.litvak.userservice.model.response.GetFavouritesResponse;
import ru.litvak.userservice.service.FavouritesService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favourites")
public class FavouritesController {

    private final FavouritesService favouritesService;

    @GetMapping()
    public GetFavouritesResponse getFriends(@RequestHeader(value = "Authorization") String authHeader) {
        return favouritesService.getFavourites(authHeader);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public void addFavourite(@RequestHeader(value = "Authorization") String authHeader,
                             @RequestBody @Valid FriendIdRequest request) {
        favouritesService.addFavourite(authHeader, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/user/{userId}")
    public void deleteFavourite(@RequestHeader(value = "Authorization") String authHeader,
                                @PathVariable UUID userId) {
        favouritesService.deleteFavourite(authHeader, userId);
    }
}
