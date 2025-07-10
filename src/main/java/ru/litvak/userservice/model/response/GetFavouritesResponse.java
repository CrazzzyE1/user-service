package ru.litvak.userservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class GetFavouritesResponse {
    private List<UUID> favouritesIds;
}
