package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.connection.CreateDatabaseConnectionDto;
import org.example.model.dto.connection.DatabaseConnectionDto;
import org.example.model.dto.connection.FullDatabaseConnectionDto;
import org.example.service.DatabaseConnectionService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/connections")
public class DatabaseConnectionController {

    private final DatabaseConnectionService databaseConnectionService;

    @PostMapping()
    public void createDatabaseConnection(Principal principal, @RequestBody CreateDatabaseConnectionDto createDatabaseConnectionDto) {
        databaseConnectionService.createDatabaseConnection(createDatabaseConnectionDto, principal.getName());
    }

    @GetMapping("/all")
    public List<DatabaseConnectionDto> getAllDatabaseConnections() {
        return databaseConnectionService.getAllDatabaseConnections();
    }

    @GetMapping("/{connectionId}")
    public FullDatabaseConnectionDto getDatabaseConnection(@PathVariable Long connectionId) {
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

