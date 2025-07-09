package ru.vldaislab.bekrenev.authservice.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.vldaislab.bekrenev.authservice.model.dto.AuthRequest;
import ru.vldaislab.bekrenev.authservice.model.dto.AuthResponse;
import ru.vldaislab.bekrenev.authservice.model.dto.RegisterRequest;
import ru.vldaislab.bekrenev.authservice.model.user.Role;
import ru.vldaislab.bekrenev.authservice.model.user.User;
import ru.vldaislab.bekrenev.authservice.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword())) // здесь важно!
                .role(Role.USER)
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }


    public AuthResponse authenticate(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new IllegalArgumentException("Неверный email или пароль");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}

