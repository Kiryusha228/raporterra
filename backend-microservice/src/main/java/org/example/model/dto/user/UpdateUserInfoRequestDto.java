package org.example.model.dto.user;

import lombok.Data;
import org.example.model.entity.user.Role;

@Data
public class UpdateUserInfoRequestDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
}