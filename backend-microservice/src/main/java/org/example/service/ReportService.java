package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.component.ResultStorage;
import org.example.config.JdbcConfig;
import org.example.exception.DatabaseConnectionNotFoundException;
import org.example.exception.InvalidReportQueryException;
import org.example.exception.ReportNotFoundException;
import org.example.exception.UserNotFoundException;
import org.example.mapper.ReportMapper;
import org.example.model.dto.report.*;
import org.example.model.entity.collection.CollectionAccess;
import org.example.model.entity.collection.CollectionReport;
import org.example.model.entity.report.Report;
import org.example.model.entity.user.Role;
import org.example.model.entity.usergroup.UserGroup;
import org.example.repository.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final DatabaseConnectionRepository databaseConnectionRepository;
    private final UserInfoRepository userRepository;
    private final ReportRepository reportRepository;
    private final ReportQueueRepository reportQueueRepository;
    private final ReportMapper reportMapper;

    private final ReportQueueService reportQueueService;

    private final JdbcConfig jdbcConfig;

    private final ResultStorage resultStorage;

    //private final RedisTemplate<String, Object> redisTemplate;

    private final UserGroupRepository userGroupRepository;
    private final CollectionAccessRepository collectionAccessRepository;
    private final CollectionReportRepository collectionReportRepository;

    public UUID createReport(CreateReportDto createReportDto, String userMail) {
        var connection = databaseConnectionRepository
                .findById(createReportDto.getConnection()).orElseThrow(
                        () -> new DatabaseConnectionNotFoundException("Соединения с таким id не найдено!")
                );

        var user = userRepository
                .findByEmail(userMail).orElseThrow(() -> new UserNotFoundException("Пользователь не найден!"));

        var report = Report.builder()
                .name(createReportDto.getName())
                .description(createReportDto.getDescription())
                .sqlQuery(createReportDto.getSqlQuery())
                .connection(connection)
                .createdBy(user).build();

        reportRepository.save(report);

        return report.getId();
    }

    public List<AvailableReportsDto> getAvailableReports(String role, String mail) {
        List<Report> reports;
        if (role.equals("ROLE_ADMIN")) {
            reports = reportRepository.findAll();
        } else {
            var user = userRepository.findByEmail(mail)
                    .orElseThrow(() -> new UserNotFoundException("Пользователь не найден!"));

            var userGroups = userGroupRepository.findByUser(user);
            var groups = userGroups.stream()
                    .map(UserGroup::getGroup)
                    .toList();

            var collectionAccess = new HashSet<>(collectionAccessRepository.findByUser(user));

            for (var group : groups) {
                collectionAccess.addAll(collectionAccessRepository.findByGroup(group));
            }

            var collections = collectionAccess.stream()
                    .map(CollectionAccess::getCollection)
                    .collect(Collectors.toSet());

            reports = collections.stream()
                    .flatMap(collection ->
                            collectionReportRepository.findByCollectionId(collection.getId()).stream()
                    )
                    .map(CollectionReport::getReport) // вытаскиваем Report из CollectionReport
                    .distinct()
                    .toList();
        }
        return reportMapper.toAvailableReportsDtoList(reports);
    }

    @Transactional
    public void updateReport(UpdateReportDto updateReportDto, UUID reportId) {
        var report = reportRepository.findById(reportId).orElseThrow(
                () -> new ReportNotFoundException("Отчет с данным UUID не найден!")
        );

        var connection = databaseConnectionRepository
                .findById(updateReportDto.getConnection()).orElseThrow(
                        () -> new DatabaseConnectionNotFoundException("Соединения с таким id не найдено!")
                );

        report.setName(updateReportDto.getName());
        report.setDescription(updateReportDto.getDescription());
        report.setSqlQuery(updateReportDto.getSqlQuery());
        report.setConnection(connection);
        report.setPublic(updateReportDto.isPublic());
    }

    public ReportMetadataDto getReport(UUID reportId) { // todo добавить функционал админ/юзер
        return reportMapper.toReportMetadataDto(
                reportRepository.findById(reportId).orElseThrow(
                        () -> new ReportNotFoundException("Отчет с данным UUID не найден!")
                )
        );
    }

    public void deleteReport(UUID reportId) {
        reportQueueRepository.deleteAll(reportQueueRepository.findAllByReportId(reportId));

        reportRepository.deleteById(reportId);
    }

    public List<Map<String, Object>> executeReport(UUID reportId) {
        var report = reportRepository.findById(reportId).orElseThrow(
                () -> new ReportNotFoundException("Отчет с данным UUID не найден!")
        );

        String redisKey = "report_result:" + reportId;

//        var cached = redisTemplate.opsForValue().get(redisKey);
//        if (cached != null) {
//            return (List<Map<String, Object>>) cached;
//        }

        if (!isSelectQuery(report.getSqlQuery())) {
            throw new InvalidReportQueryException("Разрешены только SELECT-запросы");
        }

        var jdbcTemplate = jdbcConfig.createJdbcTemplate(report.getConnection());
        var result = jdbcTemplate.queryForList(report.getSqlQuery());

        //redisTemplate.opsForValue().set(redisKey, result, Duration.ofMinutes(30));

        return result;
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

    public Optional<List<Map<String, Object>>> getResult(Long taskId) {
        var result = Optional.ofNullable(resultStorage.getResult(taskId));
        resultStorage.remove(taskId);
        return result;
    }
}
