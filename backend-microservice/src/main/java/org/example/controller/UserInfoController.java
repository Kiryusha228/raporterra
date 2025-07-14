package org.example.controller;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.example.config.JwtUtils;
import org.example.model.dto.user.CreateUserInfoDto;
import org.example.model.dto.user.UserInfoResponseDto;
import org.example.model.entity.user.UserInfo;
import org.example.repository.UserInfoRepository;
import org.example.service.UserInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    @GetMapping("/get-user-info")
    public ResponseEntity<UserInfoResponseDto> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        Claims claims = jwtUtils.extractAllClaims(authHeader);

        Long id = claims.get("id", Integer.class).longValue();
        String role = claims.get("role", String.class);

        UserInfo user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(
                new UserInfoResponseDto(
                id,
                user.getEmail(),
                role,
                user.getFirstName(),
                user.getLastName()
                )
        );
    }

}