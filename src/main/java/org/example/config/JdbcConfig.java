package org.example.config;

import lombok.RequiredArgsConstructor;
import org.example.exception.DatabaseConnectionNotFoundException;
import org.example.exception.UnsupportedDatabaseException;
import org.example.model.entity.dbconnection.DatabaseConnection;
import org.example.repository.DatabaseConnectionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@RequiredArgsConstructor
public class JdbcConfig {

    public JdbcTemplate createJdbcTemplate(DatabaseConnection connection) {
        String url = String.format("jdbc:%s://%s:%d/%s",
                connection.getDbType(),
                connection.getHost(),
                connection.getPort(),
                connection.getDatabaseName());

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(connection.getUsername());
        dataSource.setPassword(connection.getEncryptedPassword());
        dataSource.setDriverClassName(getDriverClassName(connection.getDbType()));

        return new JdbcTemplate(dataSource);
    }

    private String getDriverClassName(String dbType) {
        return switch (dbType.toLowerCase()) {
            case "postgresql" -> "org.postgresql.Driver";
            case "mysql" -> "com.mysql.cj.jdbc.Driver";
            case "oracle" -> "oracle.jdbc.OracleDriver";
            default -> throw new UnsupportedDatabaseException("Неподдерживаемый тип БД: " + dbType);
        };
    }
}
