package org.example.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
}
