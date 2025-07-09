package org.example.mapper;

import org.example.model.dto.CreateDatabaseConnectionDto;
import org.example.model.dto.DatabaseConnectionDto;
import org.example.model.entity.dbconnection.DatabaseConnection;
import org.example.model.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.HashSet;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING) //todo переделать
public class DatabaseConnectionMapper {
    public DatabaseConnection toDatabaseConnectionEntity(CreateDatabaseConnectionDto createDatabaseConnectionDto, User user){
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

    public DatabaseConnectionDto toDatabaseConnectionDto(DatabaseConnection databaseConnection) {
        var reports = new HashSet<UUID>();
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
