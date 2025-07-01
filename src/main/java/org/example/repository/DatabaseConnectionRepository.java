package org.example.repository;

import org.example.model.entity.dbconnection.DatabaseConnection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DatabaseConnectionRepository extends JpaRepository<DatabaseConnection, Long> {
    @Override
    Optional<DatabaseConnection> findById(Long id);
}
