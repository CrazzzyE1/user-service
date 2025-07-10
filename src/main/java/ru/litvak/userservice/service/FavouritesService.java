package ru.litvak.userservice.service;

import ru.litvak.userservice.model.request.FriendIdRequest;
import ru.litvak.userservice.model.response.GetFavouritesResponse;

import java.util.UUID;

public interface FavouritesService {
    GetFavouritesResponse getFavourites(String authHeader);

    void addFavourite(String authHeader, FriendIdRequest request);

    void deleteFavourite(String authHeader, UUID userId);
}
