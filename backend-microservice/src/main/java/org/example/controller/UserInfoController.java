package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.user.CreateUserInfoDto;
import org.example.service.UserInfoService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserInfoService userService;

    @PostMapping("/register")
    public void register(@RequestBody CreateUserInfoDto request) {
        userService.register(request);
    }
}