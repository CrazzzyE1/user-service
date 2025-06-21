package ru.litvak.userservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocalizedEnum {
    private String code;
    private String value;
}
