package org.example.repository;

import org.example.model.entity.collection.CollectionReport;
import org.example.model.entity.collection.CollectionReportId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CollectionReportRepository extends JpaRepository<CollectionReport, CollectionReportId> {
    @Override
    Optional<CollectionReport> findById(CollectionReportId id);
    List<CollectionReport> findByCollectionId(Long collectionId);
}
