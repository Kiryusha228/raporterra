package ru.vldaislab.bekrenev.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.vldaislab.bekrenev.authservice.model.dto.ChangeRoleDto;
import ru.vldaislab.bekrenev.authservice.model.dto.RoleResponseDto;
import ru.vldaislab.bekrenev.authservice.model.user.UserResponseDto;
import ru.vldaislab.bekrenev.authservice.model.user.Role;
import ru.vldaislab.bekrenev.authservice.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PutMapping("/role")
    void changeUserRole(@RequestBody ChangeRoleDto changeRoleDto){
        userService.changeRole(changeRoleDto);
    }

    @GetMapping("/role")
    RoleResponseDto getUserRole(@RequestParam Long userId){
        return userService.getRole(userId);
    }

    @GetMapping("/get/by-role")
    public List<UserResponseDto> getUsersByRole(Role role) {
        return userService.getUsers(role);
    }

    @GetMapping("/get/all")
    public List<UserResponseDto> getUsers() {
        return userService.getUsers();
    }

}
