package org.example.controller;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.example.config.JwtUtils;
import org.example.model.dto.user.CreateUserInfoDto;
import org.example.model.dto.user.UpdateUserInfoRequestDto;
import org.example.model.dto.user.UserInfoResponseDto;
import org.example.model.entity.user.UserInfo;
import org.example.repository.UserInfoRepository;
import org.example.service.UserInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

        Long id = claims.get("id", Long.class);
        String role = claims.get("role", String.class);

        UserInfo user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

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

    @PatchMapping("/update-user-info")
    public UserInfoResponseDto updateUserInfo(String tokenHeader, UpdateUserInfoRequestDto request) {
        Claims claims = jwtUtils.extractAllClaims(tokenHeader.replace("Bearer ", ""));
        Long userId = claims.get("id", Long.class);
        String role = claims.get("role", String.class);

        UserInfo user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        userRepository.save(user);

        return new UserInfoResponseDto(
                userId,
                user.getEmail(),
                role,
                user.getFirstName(),
                user.getLastName()
        );
    }


}