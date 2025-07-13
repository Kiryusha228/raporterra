package ru.vldaislab.bekrenev.authservice.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
