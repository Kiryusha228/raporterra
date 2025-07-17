package ru.vldaislab.bekrenev.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vldaislab.bekrenev.authservice.model.dto.ChangeRoleDto;
import ru.vldaislab.bekrenev.authservice.model.dto.RoleResponseDto;
import ru.vldaislab.bekrenev.authservice.model.user.UserResponseDto;
import ru.vldaislab.bekrenev.authservice.model.user.Role;
import ru.vldaislab.bekrenev.authservice.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void changeRole(ChangeRoleDto changeRoleDto) {
        var user = userRepository.findById(changeRoleDto.getUserId()).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        user.setRole(changeRoleDto.getRole());
        userRepository.save(user);
    }

    public RoleResponseDto getRole(Long userId) {
        return new RoleResponseDto(userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User not found")
        ).getRole());
    }

    public List<UserResponseDto> getUsers(Role role) {
        var users = new ArrayList<UserResponseDto>();
        for (var user : userRepository.findAllByRole(role)) {
            users.add(new UserResponseDto(user.getId(),user.getRole()));
        }
        return users;
    }

    public List<UserResponseDto> getUsers() {
        var users = new ArrayList<UserResponseDto>();
        for (var user : userRepository.findAll()) {
            users.add(new UserResponseDto(user.getId(),user.getRole()));
        }
        return users;
    }

}
