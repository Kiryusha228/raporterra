package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.CreateDatabaseConnectionDto;
import org.example.dto.DatabaseConnectionDto;
import org.example.service.DatabaseConnectionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/connections")
public class DatabaseConnectionController {

    private final DatabaseConnectionService databaseConnectionService;

    @PostMapping()
    public void createDatabaseConnection(@RequestBody CreateDatabaseConnectionDto createDatabaseConnectionDto, Long userId) {
        databaseConnectionService.createDatabaseConnection(createDatabaseConnectionDto, userId);
    }

    @GetMapping()
    public List<DatabaseConnectionDto> getAllDatabaseConnections() {
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

