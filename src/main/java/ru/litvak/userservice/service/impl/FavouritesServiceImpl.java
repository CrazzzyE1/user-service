package ru.litvak.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.litvak.userservice.manager.FavouritesManager;
import ru.litvak.userservice.model.request.FriendIdRequest;
import ru.litvak.userservice.model.response.GetFavouritesResponse;
import ru.litvak.userservice.service.FavouritesService;
import ru.litvak.userservice.util.JwtTokenMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FavouritesServiceImpl implements FavouritesService {

    private final FavouritesManager favouritesManager;

    @Override
    public GetFavouritesResponse getFavourites(String authHeader) {
        UUID me = JwtTokenMapper.map(authHeader).getId();
        return favouritesManager.get(me);
    }

    @Override
    public void addFavourite(String authHeader, FriendIdRequest request) {
        UUID me = JwtTokenMapper.map(authHeader).getId();
        favouritesManager.add(me, request.getFriendId());
    }

    @Override
    public void deleteFavourite(String authHeader, UUID userId) {
        UUID me = JwtTokenMapper.map(authHeader).getId();
        favouritesManager.delete(me, userId);
    }
}
