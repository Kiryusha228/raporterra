package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.report.*;
import org.example.model.entity.report.TaskStatus;
import org.example.model.entity.user.Role;
import org.example.service.ReportQueueService;
import org.example.service.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;
    private final ReportQueueService reportQueueService;

    @PostMapping()
    public UUID createReport(Principal principal, @RequestBody CreateReportDto createReportDto) {
        return reportService.createReport(createReportDto, principal.getName());
    }

    @GetMapping("/get")
    public List<AvailableReportsDto> getAvailableReports(Authentication authentication) {
        var role = authentication.getAuthorities().stream().toList().get(0).toString();
        var mail = authentication.getName();
        return reportService.getAvailableReports(role, mail);
    }

    @PutMapping("/{reportId}")
    public void updateReport(@RequestBody UpdateReportDto updateReportDto, @PathVariable UUID reportId) {
        reportService.updateReport(updateReportDto, reportId);
    }

    @GetMapping("/{reportId}")
    public ReportMetadataDto getReport(@PathVariable UUID reportId) {
        return reportService.getReport(reportId);
    }

    @DeleteMapping("/{reportId}")
    public void deleteReport(@PathVariable UUID reportId) {
         reportService.deleteReport(reportId);
    }

    @PostMapping("/execute/{reportId}")
    public ReportQueueResultDto executeReport(@PathVariable UUID reportId) {
        return reportService.setToQueue(reportId);
    }

    @PostMapping("/execute/{taskId}/get")
    public ResponseEntity<?> checkReport(@PathVariable Long taskId) {
        var result = reportService.getResult(taskId);
        if (result.isEmpty()){
            var status = reportQueueService.getStatus(taskId);
            if (status == TaskStatus.FAILED) {
                return ResponseEntity.ok(status);
            }
            else {
                return ResponseEntity.ok(reportQueueService.getQueueSize());
            }
        }
        return ResponseEntity.ok(result.get());
    }

}
