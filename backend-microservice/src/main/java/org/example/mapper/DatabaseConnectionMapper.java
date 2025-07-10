package org.example.mapper;

import org.example.model.dto.connection.CreateDatabaseConnectionDto;
import org.example.model.dto.connection.DatabaseConnectionDto;
import org.example.model.dto.connection.FullDatabaseConnectionDto;
import org.example.model.entity.dbconnection.DatabaseConnection;
import org.example.model.entity.report.Report;
import org.example.model.entity.user.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING) //todo переделать
public interface DatabaseConnectionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "reports", ignore = true)
    @Mapping(target = "createdBy", source = "user")
    DatabaseConnection toEntity(CreateDatabaseConnectionDto dto, UserInfo user);

    @Mapping(target = "createdBy", source = "createdBy.id")
    @Mapping(target = "reports", source = "reports")
    FullDatabaseConnectionDto toFullDatabaseConnectionDto(DatabaseConnection databaseConnection);

    default Set<UUID> mapReportsToIds(Set<Report> reports) {
        if (reports == null) return Set.of();
        return reports.stream()
                .map(Report::getId)
                .collect(Collectors.toSet());
    }

    public DatabaseConnectionDto toDatabaseConnectionDto(DatabaseConnection databaseConnection);

}
