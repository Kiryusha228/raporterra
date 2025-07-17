package org.example.model.dto.user;

import lombok.Data;

@Data
public class UpdateUserInfoRequestDto {
    private String email;
    private String firstName;
    private String lastName;
    private String role;

}