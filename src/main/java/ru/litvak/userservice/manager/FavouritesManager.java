package ru.litvak.userservice.manager;

import ru.litvak.userservice.model.response.GetFavouritesResponse;

import java.util.UUID;

public interface FavouritesManager {
    GetFavouritesResponse get(UUID me);

    void add(UUID me, UUID friendId);

    void delete(UUID me, UUID userId);
}
