package org.example.mapper;

import org.example.dto.CreateDatabaseConnectionDto;
import org.example.dto.DatabaseConnectionDto;
import org.example.entities.dbconnection.DatabaseConnection;
import org.example.entities.user.User;

import java.util.HashSet;

public class DatabaseConnectionMapper {
    public static DatabaseConnection toDatabaseConnectionEntity(CreateDatabaseConnectionDto createDatabaseConnectionDto, User user){
         return DatabaseConnection.builder()
                .name(createDatabaseConnectionDto.getName())
                .dbType(createDatabaseConnectionDto.getDbType())
                .host(createDatabaseConnectionDto.getHost())
                .port(createDatabaseConnectionDto.getPort())
                .username(createDatabaseConnectionDto.getUsername())
                .databaseName(createDatabaseConnectionDto.getDatabaseName())
                .encryptedPassword(createDatabaseConnectionDto.getEncryptedPassword())
                .createdBy(user).build();
    }

    public static DatabaseConnectionDto toDatabaseConnectionDto(DatabaseConnection databaseConnection) {
        var reports = new HashSet<Long>();
        for (var report : databaseConnection.getReports()) {
            reports.add(report.getId());
        }

        return DatabaseConnectionDto.builder()
                .name(databaseConnection.getName())
                .dbType(databaseConnection.getDbType())
                .host(databaseConnection.getHost())
                .port(databaseConnection.getPort())
                .username(databaseConnection.getUsername())
                .databaseName(databaseConnection.getDatabaseName())
                .encryptedPassword(databaseConnection.getEncryptedPassword())
                .createdBy(databaseConnection.getCreatedBy().getId())
                .createdAt(databaseConnection.getCreatedAt())
                .reports(reports)
                .build();
    }

}
