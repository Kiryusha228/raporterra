package org.example.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.model.entity.user.Role;

@Data
@AllArgsConstructor
public class UserInfoResponseDto {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
}

