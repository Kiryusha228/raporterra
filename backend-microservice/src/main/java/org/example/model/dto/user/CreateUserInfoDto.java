package org.example.model.dto.user;

import lombok.Data;

@Data
public class CreateUserInfoDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
}
