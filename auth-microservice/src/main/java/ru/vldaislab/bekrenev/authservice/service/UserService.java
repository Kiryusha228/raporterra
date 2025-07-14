package ru.vldaislab.bekrenev.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vldaislab.bekrenev.authservice.model.user.Role;
import ru.vldaislab.bekrenev.authservice.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

}
