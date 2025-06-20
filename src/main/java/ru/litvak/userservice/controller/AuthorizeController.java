package ru.litvak.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.litvak.userservice.service.AuthorizeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authorize")
public class AuthorizeController {

    private final AuthorizeService authorizeService;

    @PostMapping()
    public void authorize(@RequestHeader(value = "Authorization") String authHeader) throws Exception {
        authorizeService.authorize(authHeader);
    }
}
