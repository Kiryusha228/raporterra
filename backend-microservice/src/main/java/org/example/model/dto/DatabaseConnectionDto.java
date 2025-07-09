package org.example.model.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseConnectionDto {
    private String name;

    private String dbType;

    private String host;

    private Integer port;

    private String databaseName;

    private String username;

    private String encryptedPassword;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private Long createdBy;

    @Builder.Default
    private boolean isActive = true;

    @Builder.Default
    private Set<UUID> reports = new HashSet<>();
}
