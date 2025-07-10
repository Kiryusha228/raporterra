package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.report.*;
import org.example.service.ReportQueueService;
import org.example.service.ReportService;
import org.springframework.web.bind.annotation.*;

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
    public UUID createReport(@RequestBody CreateReportDto createReportDto, Long userId) {
        return reportService.createReport(createReportDto, userId);
    }

    @GetMapping()
    public List<AvailableReportsDto> getAvailableReports() {
        return reportService.getAvailableReports();
    }

    @PutMapping("/{reportId}")
    public void updateReport(@RequestBody UpdateReportDto updateReportDto, @PathVariable UUID reportId, Long userId) {
        reportService.updateReport(updateReportDto, userId, reportId);
    }

    @GetMapping("/{reportId}")
    public ReportMetadataDto getReport(@PathVariable UUID reportId) {
        return reportService.getReport(reportId);
    }

    @DeleteMapping("/{reportId}")
    public void deleteReport(@PathVariable UUID reportId) {
         reportService.deleteReport(reportId);
    }

    @PostMapping("/{reportId}/execute")
    public ReportQueueResultDto executeReport(@PathVariable UUID reportId) {
        return reportService.setToQueue(reportId);
    }

    @PostMapping("/{taskId}/get")
    public List<Map<String, Object>> executeReport(@PathVariable Long taskId) {
        return reportService.getResult(taskId);
    }

}
