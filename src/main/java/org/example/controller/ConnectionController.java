package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.ConnectionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/connections")
public class ConnectionController {
    private final ConnectionService connectionService;
    @PostMapping("")
    public void connectionTest(Long connectionId) {
        connectionService.connect(connectionId);
    }
}

