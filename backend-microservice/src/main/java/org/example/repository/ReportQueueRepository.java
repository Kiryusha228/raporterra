package org.example.repository;

import jakarta.transaction.Transactional;
import org.example.model.entity.report.ReportQueue;
import org.example.model.entity.report.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReportQueueRepository extends JpaRepository<ReportQueue, Long> {

    @Transactional
    @Query(value = """
        SELECT * FROM report_queue
        WHERE status = 'PENDING'
        ORDER BY task_id
        FOR UPDATE SKIP LOCKED
        LIMIT 1
        """, nativeQuery = true)
    Optional<ReportQueue> findNextPending();

    Long countByStatus(TaskStatus status);
}

