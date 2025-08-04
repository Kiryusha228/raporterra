package ru.vldaislab.bekrenev.authservice.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import ru.vldaislab.bekrenev.authservice.annotations.email.EmailPostfix;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    @EmailPostfix(postfix = "@tbank.ru", message = "Разрешены только корпоративные email")
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, message = "Пароль должен содержать не менее 8 символов")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "Пароль должен содержать цифру, буквы в верхнем и нижнем регистре и спецсимвол (@#$%^&+=!)"
    )
    private String password;
}

