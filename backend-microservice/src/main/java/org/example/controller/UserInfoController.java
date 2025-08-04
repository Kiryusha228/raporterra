package org.example.controller;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.example.config.JwtUtils;
import org.example.model.dto.user.CreateUserInfoDto;
import org.example.model.dto.user.UpdateUserInfoRequestDto;
import org.example.model.dto.user.UserInfoResponseDto;
import org.example.model.entity.user.Role;
import org.example.model.entity.user.UserInfo;
import org.example.repository.UserInfoRepository;
import org.example.service.UserInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserInfoService userService;
    private final JwtUtils jwtUtils;
    private final UserInfoRepository userRepository;

    @PostMapping("/register")
    public void register(@RequestBody CreateUserInfoDto request) {
        userService.register(request);
    }

    @GetMapping("/me")
    public UserInfoResponseDto getUserInfo(Principal principal) {
        return userService.getUserInfo(principal.getName());
    }

    @GetMapping("/get/by-role")
    public List<UserInfoResponseDto> getUsersByRole(Role role) {
        return userService.getUsersByRole(role);
    }

    @GetMapping("/get/all")
    public List<UserInfoResponseDto> getUsers() {
        return userService.getUsers();
    }





}