package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.CreateDatabaseConnectionDto;
import org.example.dto.DatabaseConnectionDto;
import org.example.entities.dbconnection.DatabaseConnection;
import org.example.mapper.DatabaseConnectionMapper;
import org.example.repository.DatabaseConnectionRepository;
import org.example.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseConnectionService {
    private final UserRepository userRepository;
    private final DatabaseConnectionRepository databaseConnectionRepository;

    public void createDatabaseConnection(CreateDatabaseConnectionDto createDatabaseConnectionDto, Long userid){
        var userOptional = userRepository.findById(userid);
        if (userOptional.isEmpty()) {
            throw new NullPointerException("Пользователя с указанным id не существует");
        }
        var user = userOptional.get();
        databaseConnectionRepository.save(
                DatabaseConnectionMapper.toDatabaseConnectionEntity(createDatabaseConnectionDto, user)
        );
    }

    public DatabaseConnectionDto getDatabaseConnection(Long databaseConnectionId) {
        var databaseConnectionOptional = databaseConnectionRepository.findById(databaseConnectionId);
        if (databaseConnectionOptional.isEmpty()) {
            throw new NullPointerException("Подключения с данным id не существует");
        }

        return DatabaseConnectionMapper.toDatabaseConnectionDto(databaseConnectionOptional.get());
    }

    public List<DatabaseConnectionDto> getAllDatabaseConnections() {
        var databaseConnections = databaseConnectionRepository.findAll();
        var databaseConnectionsDto = new ArrayList<DatabaseConnectionDto>();
        for (var connection : databaseConnections) {
            databaseConnectionsDto.add(DatabaseConnectionMapper.toDatabaseConnectionDto(connection));
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

        var jdbcTemplate = getJdbcTemplate(connectionOptional.get());
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class); //todo выводить ошибку
            return true;
        }
        catch (Exception e){
            return false;
        }

    }

    private JdbcTemplate getJdbcTemplate(DatabaseConnection databaseConnection) {
        var link = "jdbc:" + databaseConnection.getDbType() + "://" + databaseConnection.getHost()+
                ":" + databaseConnection.getPort() + "/" + databaseConnection.getDatabaseName();

        var dataSource = new DriverManagerDataSource(link,
                databaseConnection.getUsername(), databaseConnection.getEncryptedPassword());

        dataSource.setDriverClassName("org.postgresql.Driver"); //todo разобраться с драйверами

        return new JdbcTemplate(dataSource);
    }
}
