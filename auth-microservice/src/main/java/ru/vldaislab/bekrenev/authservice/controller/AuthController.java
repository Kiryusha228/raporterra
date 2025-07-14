package ru.vldaislab.bekrenev.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vldaislab.bekrenev.authservice.model.dto.AuthRequest;
import ru.vldaislab.bekrenev.authservice.model.dto.AuthResponse;
import ru.vldaislab.bekrenev.authservice.model.dto.RegisterRequest;
import ru.vldaislab.bekrenev.authservice.service.AuthService;
import ru.vldaislab.bekrenev.authservice.service.MailSenderService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MailSenderService mailSender;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            mailSender.sendRegistrationEmail(request.getEmail(), request.getFirstName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Ошибка регистрации: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest request) {
        try {
            AuthResponse response = authService.authenticate(request);
            mailSender.sentLoginEmail(request.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Ошибка входа: " + e.getMessage());
        }
    }
}
