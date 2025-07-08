package org.example.repository;

import org.example.model.entity.report.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, UUID> {
    @Override
    Optional<Report> findById(UUID id);
    List<Report> findByIsPublicTrue();
}
