package org.example.model.dto.connection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDatabaseConnectionDto {
    private String name;

    private String dbType;

    private String host;

    private Integer port;

    private String databaseName;

    private String username;

    private String encryptedPassword;
}
