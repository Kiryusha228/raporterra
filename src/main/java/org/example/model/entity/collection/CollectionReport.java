package org.example.model.entity.collection;

import jakarta.persistence.*;
import lombok.*;
import org.example.model.entity.report.Report;

@Entity
@Table(name = "collection_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectionReport {
    @EmbeddedId
    private CollectionReportId id;

    @ManyToOne
    @MapsId("collectionId")
    @JoinColumn(name = "collection_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Collection collection;

    @ManyToOne
    @MapsId("reportId")
    @JoinColumn(name = "report_id", columnDefinition = "uuid")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Report report;
}
