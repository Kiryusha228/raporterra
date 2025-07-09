package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.component.ResultStorage;
import org.example.config.JdbcConfig;
import org.example.exception.DatabaseConnectionNotFoundException;
import org.example.exception.InvalidReportQueryException;
import org.example.exception.ReportNotFoundException;
import org.example.exception.UserNotFoundException;
import org.example.mapper.ReportMapper;
import org.example.model.dto.report.*;
import org.example.model.entity.report.Report;
import org.example.repository.DatabaseConnectionRepository;
import org.example.repository.ReportRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final DatabaseConnectionRepository databaseConnectionRepository;
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;

    private final ReportQueueService reportQueueService;

    private final JdbcConfig jdbcConfig;

    private final ResultStorage resultStorage;

    public UUID createReport(CreateReportDto createReportDto, Long userId) {
        var connection = databaseConnectionRepository
                .findById(createReportDto.getConnection()).orElseThrow(
                        () -> new DatabaseConnectionNotFoundException("Соединения с таким id не найдено!")
                );

        var user = userRepository
                .findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь с таким id не найден!"));

        var report = Report.builder()
                .name(createReportDto.getName())
                .description(createReportDto.getDescription())
                .sqlQuery(createReportDto.getSqlQuery())
                .connection(connection)
                .createdBy(user).build();

        reportRepository.save(report);

        return report.getId();
    }

    public List<AvailableReportsDto> getAvailableReports() {
        return reportMapper.toAvailableReportsDtoList(reportRepository.findByIsPublicTrue());
    }

    public void updateReport(UpdateReportDto updateReportDto, Long userId, UUID reportId) {
        var report = reportRepository.findById(reportId).orElseThrow(
                () -> new ReportNotFoundException("Отчет с данным UUID не найден!")
        );

        var user = userRepository
                .findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь с таким id не найден!"));

        var connection = databaseConnectionRepository
                .findById(updateReportDto.getConnection()).orElseThrow(
                        () -> new DatabaseConnectionNotFoundException("Соединения с таким id не найдено!")
                );

        report.setName(updateReportDto.getName());
        report.setDescription(updateReportDto.getDescription());
        report.setSqlQuery(updateReportDto.getSqlQuery());
        report.setConnection(connection);
        report.setUpdatedAt(LocalDateTime.now());
        report.setUpdatedBy(user);
        report.setPublic(updateReportDto.isPublic());

        reportRepository.save(report);
    }

    public ReportMetadataDto getReport(UUID reportId) { // todo добавить функционал админ/юзер
        return reportMapper.toReportMetadataDto(
                reportRepository.findById(reportId).orElseThrow(
                        () -> new ReportNotFoundException("Отчет с данным UUID не найден!")
                )
        );
    }

    public void deleteReport(UUID reportId) {
        reportRepository.deleteById(reportId);
    }

    public List<Map<String, Object>> executeReport(UUID reportId) {
        var report = reportRepository.findById(reportId).orElseThrow(
                () -> new ReportNotFoundException("Отчет с данным UUID не найден!")
        );

        var jdbcTemplate = jdbcConfig.createJdbcTemplate(report.getConnection());

        if (!isSelectQuery(report.getSqlQuery())) {
            throw new InvalidReportQueryException("Разрешены только SELECT-запросы");
        }

        return jdbcTemplate.queryForList(report.getSqlQuery());
    }

    private boolean isSelectQuery(String sql) { // todo мб можно сделать более правильно
        String normalized = sql.trim().toUpperCase();
        return normalized.startsWith("SELECT")
                && !normalized.contains(";")
                && !normalized.contains("EXEC")
                && !normalized.contains("CALL");
    }

    public ReportQueueResultDto setToQueue(UUID reportId) {
        var report = reportRepository.findById(reportId).orElseThrow(
                () -> new ReportNotFoundException("Отчет с данным UUID не найден!")
        );

        return reportQueueService.enqueue(report);
    }

    public List<Map<String, Object>> getResult(Long taskId) {
        var result = resultStorage.getResult(taskId);
        resultStorage.remove(taskId);
        return result;  //todo придумать что делать если еще не выполнено
    }
}
