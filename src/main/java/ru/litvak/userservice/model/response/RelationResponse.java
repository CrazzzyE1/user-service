package ru.litvak.userservice.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.litvak.userservice.enumerated.PrivacyLevel;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RelationResponse {
    private PrivacyLevel privacyLevel;

    @JsonProperty("isFriends")
    private boolean isFriends;

    @JsonProperty("isFavourites")
    private boolean isFavourites;

    @JsonProperty("hasIncomeFriendsRequest")
    private boolean hasIncomeFriendsRequest;

    @JsonProperty("hasOutcomeFriendsRequest")
    private boolean hasOutcomeFriendsRequest;
}
