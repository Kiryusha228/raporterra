package org.example.model.dto.report;

import lombok.Data;

@Data
public class ReportQueueResultDto {
    private Long taskId;
    private Long queueSize;
}
