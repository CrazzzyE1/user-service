package ru.litvak.userservice.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.litvak.userservice.enumerated.PrivacyLevel;

@Getter
@Setter
@AllArgsConstructor
public class RelationResponse {
    private PrivacyLevel privacyLevel;
    @JsonProperty("isFriend")
    private boolean isFriend;
}
