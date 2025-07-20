package org.example.component;

import lombok.RequiredArgsConstructor;
import org.example.model.entity.report.TaskStatus;
import org.example.service.ReportQueueService;
import org.example.service.ReportService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportQueueWorker {
    private final ReportQueueService reportQueueService;
    private final ReportService reportService;
    private final ResultStorage resultStorage;

    @Scheduled(fixedDelay = 2000)
    public void execute() {
        reportQueueService.getNextPending().ifPresent(task -> {
            try {
                reportQueueService.updateStatus(task.getId(), TaskStatus.PROCESSING);

                var result = reportService.executeReport(task.getReport().getId());

                resultStorage.putResult(task.getId(), result);
                reportQueueService.updateStatus(task.getId(), TaskStatus.DONE);
            } catch (Exception e) {
                reportQueueService.updateStatus(task.getId(), TaskStatus.FAILED);
                e.printStackTrace();
            }
        });
    }
}
