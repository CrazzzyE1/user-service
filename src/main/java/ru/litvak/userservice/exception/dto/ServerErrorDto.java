package ru.litvak.userservice.exception.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ServerErrorDto {
    private String message;
}
