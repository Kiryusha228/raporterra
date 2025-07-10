package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.config.JdbcConfig;
import org.example.model.dto.CreateDatabaseConnectionDto;
import org.example.model.dto.DatabaseConnectionDto;
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

    public void createDatabaseConnection(CreateDatabaseConnectionDto createDatabaseConnectionDto, Long userid){
        var userOptional = userRepository.findById(userid);
        if (userOptional.isEmpty()) {
            throw new NullPointerException("Пользователя с указанным id не существует");
        }
        var user = userOptional.get();
        databaseConnectionRepository.save(
                databaseConnectionMapper.toDatabaseConnectionEntity(createDatabaseConnectionDto, user)
        );
    }

    public DatabaseConnectionDto getDatabaseConnection(Long databaseConnectionId) {
        var databaseConnectionOptional = databaseConnectionRepository.findById(databaseConnectionId);
        if (databaseConnectionOptional.isEmpty()) {
            throw new NullPointerException("Подключения с данным id не существует");
        }

        return databaseConnectionMapper.toDatabaseConnectionDto(databaseConnectionOptional.get());
    }

    public List<DatabaseConnectionDto> getAllDatabaseConnections() {
        var databaseConnections = databaseConnectionRepository.findAll();
        var databaseConnectionsDto = new ArrayList<DatabaseConnectionDto>();
        for (var connection : databaseConnections) {
            databaseConnectionsDto.add(databaseConnectionMapper.toDatabaseConnectionDto(connection));
        }

        return databaseConnectionsDto;
    }

    public void updateDatabaseConnection(Long databaseConnectionId, CreateDatabaseConnectionDto createDatabaseConnectionDto) {
        var databaseConnectionOptional = databaseConnectionRepository.findById(databaseConnectionId);
        if (databaseConnectionOptional.isEmpty()) {
            throw new NullPointerException("Подключения с данным id не существует");
        }

        var databaseConnection = databaseConnectionOptional.get();
        databaseConnection.setName(createDatabaseConnectionDto.getName());
        databaseConnection.setDbType(createDatabaseConnectionDto.getDbType());
        databaseConnection.setHost(createDatabaseConnectionDto.getHost());
        databaseConnection.setPort(createDatabaseConnectionDto.getPort());
        databaseConnection.setDatabaseName(createDatabaseConnectionDto.getDatabaseName());
        databaseConnection.setUsername(createDatabaseConnectionDto.getUsername());
        databaseConnection.setEncryptedPassword(createDatabaseConnectionDto.getEncryptedPassword());

        databaseConnectionRepository.save(databaseConnection);
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
