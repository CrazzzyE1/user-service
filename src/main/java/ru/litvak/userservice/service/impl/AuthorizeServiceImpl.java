package ru.litvak.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.litvak.userservice.manager.UserProfileManager;
import ru.litvak.userservice.service.AuthorizeService;
import ru.litvak.userservice.util.JwtTokenMapper;

@Service
@RequiredArgsConstructor
public class AuthorizeServiceImpl implements AuthorizeService {

    private final UserProfileManager userProfileManager;

    @Override
    public void authorize(String authHeader) {
        userProfileManager.update(JwtTokenMapper.map(authHeader));
    }
}
