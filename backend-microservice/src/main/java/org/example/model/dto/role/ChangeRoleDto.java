package org.example.model.dto.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.entity.user.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRoleDto {
    private Long userId;
    private Role role;
}
