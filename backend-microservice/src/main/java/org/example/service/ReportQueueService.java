package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.report.ReportQueueResultDto;
import org.example.model.entity.report.Report;
import org.example.model.entity.report.ReportQueue;
import org.example.model.entity.report.TaskStatus;
import org.example.repository.ReportQueueRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportQueueService {
    private final ReportQueueRepository reportQueueRepository;

    @Transactional
    public ReportQueueResultDto enqueue(Report report) {
        var task = new ReportQueue();
        task.setReport(report);

        reportQueueRepository.save(task);

        var result = new ReportQueueResultDto();
        result.setTaskId(task.getId());
        result.setQueueSize(reportQueueRepository.countByStatus(TaskStatus.PENDING));

        return result;
    }

    @Transactional
    public Optional<ReportQueue> getNextPending() {
        return reportQueueRepository.findNextPending();
    }

    @Transactional
    public void updateStatus(Long taskId, TaskStatus status) {
        reportQueueRepository.findById(taskId).ifPresent(task -> task.setStatus(status));
    }
}
