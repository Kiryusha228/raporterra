package ru.vldaislab.bekrenev.authservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vldaislab.bekrenev.authservice.model.user.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRoleDto {
    private Long userId;
    private Role role;
}
