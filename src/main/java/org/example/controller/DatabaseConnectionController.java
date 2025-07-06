package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.CreateDatabaseConnectionDto;
import org.example.model.dto.DatabaseConnectionDto;
import org.example.service.DatabaseConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/connections")
public class DatabaseConnectionController {
    private static final Logger log = LoggerFactory.getLogger(DatabaseConnectionController.class);
    private final DatabaseConnectionService databaseConnectionService;

    @PostMapping()
    public void createDatabaseConnection(@RequestBody CreateDatabaseConnectionDto createDatabaseConnectionDto, Long userId) {
        databaseConnectionService.createDatabaseConnection(createDatabaseConnectionDto, userId);
    }

    @GetMapping()
    public List<DatabaseConnectionDto> getAllDatabaseConnections() {
        MDC.put("userId", "user-123");
        MDC.put("requestId", "1");

        log.info("This will be sent to Graylog");
        log.error("Error example", new RuntimeException("Test error"));

        MDC.clear();
        return databaseConnectionService.getAllDatabaseConnections();
    }

    @GetMapping("/{connectionId}")
    public DatabaseConnectionDto getDatabaseConnection(@PathVariable Long connectionId) {
        return databaseConnectionService.getDatabaseConnection(connectionId);
    }

    @PutMapping("/{connectionId}")
    public void updateDatabaseConnection(@PathVariable Long connectionId, @RequestBody CreateDatabaseConnectionDto createDatabaseConnectionDto) {
        databaseConnectionService.updateDatabaseConnection(connectionId, createDatabaseConnectionDto);
    }

    @PostMapping("/{connectionId}/test")
    public boolean testDatabaseConnection(@PathVariable Long connectionId) {
        return databaseConnectionService.testConnection(connectionId);
    }
}

