package ru.litvak.userservice.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.litvak.userservice.enumerated.StatusType;

@Getter
@Setter
public class UpdateUserStatusRequest {

    @NotNull
    private StatusType status;
}
