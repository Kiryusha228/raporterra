package org.example.model.dto.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.dto.user.UserInfoResponseDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupForUserDto {
    private GroupDto group;
    private List<UserInfoResponseDto> users;
}
