package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entities.dbconnection.DatabaseConnection;
import org.example.repository.DatabaseConnectionRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConnectionService {
    private final DatabaseConnectionRepository databaseConnectionRepository;

    public void connect(Long connectionId) {
        var connectionOptional = databaseConnectionRepository.findById(connectionId);

        var jdbcTemplate = getJdbcTemplate(connectionOptional);

        //jdbc:postgresql://localhost:5432/raporterra_db
        List<Map<String, Object>> users = jdbcTemplate.queryForList("select * from database_connections");

        users.forEach(row -> {
            row.forEach((column, value) -> {
                System.out.println(column + ": " + value);
            });
            System.out.println("-----");
        });
    }

    private static JdbcTemplate getJdbcTemplate(Optional<DatabaseConnection> connectionOptional) {
        if (connectionOptional.isEmpty()) {
            throw new NullPointerException("Соединения с таким номером нет");
        }
        var connectionEntity = connectionOptional.get();

        var link = "jdbc:" + connectionEntity.getDbType() + "://" + connectionEntity.getHost()+
                ":" + connectionEntity.getPort() + "/" + connectionEntity.getDatabaseName();

        var dataSource = new DriverManagerDataSource(link,
                connectionEntity.getUsername(), connectionEntity.getEncryptedPassword());

        dataSource.setDriverClassName("org.postgresql.Driver"); //todo разобраться с драйверами

        return new JdbcTemplate(dataSource);
    }
}
