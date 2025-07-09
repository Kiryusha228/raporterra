package ru.vldaislab.bekrenev.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vldaislab.bekrenev.authservice.model.dto.AuthRequest;
import ru.vldaislab.bekrenev.authservice.model.dto.AuthResponse;
import ru.vldaislab.bekrenev.authservice.model.dto.RegisterRequest;
import ru.vldaislab.bekrenev.authservice.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
