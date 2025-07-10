package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.config.JdbcConfig;
import org.example.exception.DatabaseConnectionNotFoundException;
import org.example.model.dto.connection.CreateDatabaseConnectionDto;
import org.example.model.dto.connection.DatabaseConnectionDto;
import org.example.model.dto.connection.FullDatabaseConnectionDto;
import org.example.mapper.DatabaseConnectionMapper;
import org.example.repository.DatabaseConnectionRepository;
import org.example.repository.UserInfoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseConnectionService {
    private final UserInfoRepository userRepository;
    private final DatabaseConnectionRepository databaseConnectionRepository;
    private final JdbcConfig jdbcConfig;
    private final DatabaseConnectionMapper databaseConnectionMapper;

    public void createDatabaseConnection(CreateDatabaseConnectionDto createDatabaseConnectionDto, String userMail){
        var user = userRepository.findByEmail(userMail).orElseThrow(
                () -> new NullPointerException("Пользователя не существует")
        );
        databaseConnectionRepository.save(
                databaseConnectionMapper.toEntity(createDatabaseConnectionDto, user)
        );
    }

    public FullDatabaseConnectionDto getDatabaseConnection(Long databaseConnectionId) {
        var databaseConnection = databaseConnectionRepository.findById(databaseConnectionId).orElseThrow(
                () -> new DatabaseConnectionNotFoundException("Подключения с данным id не существует")
        );

        return databaseConnectionMapper.toFullDatabaseConnectionDto(databaseConnection);
    }

    public List<DatabaseConnectionDto> getAllDatabaseConnections() {
        var databaseConnections = databaseConnectionRepository.findAll();
        var databaseConnectionsDto = new ArrayList<DatabaseConnectionDto>();
        for (var connection : databaseConnections) {
            databaseConnectionsDto.add(databaseConnectionMapper.toDatabaseConnectionDto(connection));
        }

        return databaseConnectionsDto;
    }

    @Transactional
    public void updateDatabaseConnection(Long databaseConnectionId, CreateDatabaseConnectionDto createDatabaseConnectionDto) {
        var databaseConnection = databaseConnectionRepository.findById(databaseConnectionId).orElseThrow(
                () -> new DatabaseConnectionNotFoundException("Подключения с данным id не существует")
        );

        databaseConnection.setName(createDatabaseConnectionDto.getName());
        databaseConnection.setDbType(createDatabaseConnectionDto.getDbType());
        databaseConnection.setHost(createDatabaseConnectionDto.getHost());
        databaseConnection.setPort(createDatabaseConnectionDto.getPort());
        databaseConnection.setDatabaseName(createDatabaseConnectionDto.getDatabaseName());
        databaseConnection.setUsername(createDatabaseConnectionDto.getUsername());
        databaseConnection.setEncryptedPassword(createDatabaseConnectionDto.getEncryptedPassword());
    }

    public boolean testConnection(Long connectionId){
        var connectionOptional = databaseConnectionRepository.findById(connectionId);
        if (connectionOptional.isEmpty()) {
            throw new NullPointerException("Соединения с таким номером нет");
        }

        var jdbcTemplate = jdbcConfig.createJdbcTemplate(connectionOptional.get());

        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class); //todo выводить ошибку
            return true;
        }
        catch (Exception e){
            return false;
        }

    }
}
