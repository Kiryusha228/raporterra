package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.dto.CreateDatabaseConnectionDto;
import org.example.model.dto.DatabaseConnectionDto;
import org.example.service.DatabaseConnectionService;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/connections")
public class DatabaseConnectionController {
    private final DatabaseConnectionService databaseConnectionService;

    @PostMapping()
    public void createDatabaseConnection(@RequestBody CreateDatabaseConnectionDto createDatabaseConnectionDto, Long userId) {
        MDC.put("userId", userId.toString());
        log.info("Пользователь создал подключение");
        MDC.clear();
        databaseConnectionService.createDatabaseConnection(createDatabaseConnectionDto, userId);
    }

    @GetMapping()
    public List<DatabaseConnectionDto> getAllDatabaseConnections() {
        log.info("Кто то получил все подключения");
        return databaseConnectionService.getAllDatabaseConnections();
    }

    @GetMapping("/{connectionId}")
    public DatabaseConnectionDto getDatabaseConnection(@PathVariable Long connectionId) {
        log.info("Кто то получил подключение с номером {}", connectionId.toString());
        return databaseConnectionService.getDatabaseConnection(connectionId);
    }

    @PutMapping("/{connectionId}")
    public void updateDatabaseConnection(@PathVariable Long connectionId, @RequestBody CreateDatabaseConnectionDto createDatabaseConnectionDto) {
        log.info("Кто то изменил подключение с номером {}", connectionId.toString());
        databaseConnectionService.updateDatabaseConnection(connectionId, createDatabaseConnectionDto);
    }

    @PostMapping("/{connectionId}/test")
    public boolean testDatabaseConnection(@PathVariable Long connectionId) {
        log.info("Кто то проверил подключение с номером {}", connectionId.toString());
        return databaseConnectionService.testConnection(connectionId);
    }
}

