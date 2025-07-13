package ru.vldaislab.bekrenev.authservice.model.dto;

import org.springframework.stereotype.Component;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    private String email;
    private String password;
}

