package org.example.model.dto.group;

import jakarta.persistence.*;
import lombok.*;
import org.example.model.entity.user.UserInfo;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt = LocalDateTime.now();
    private Long createdBy;
}
