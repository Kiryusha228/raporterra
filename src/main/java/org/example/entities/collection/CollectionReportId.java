package org.example.entities.collection;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectionReportId implements Serializable {
    @Column(name = "collection_id")
    private Long collectionId;

    @Column(name = "report_id")
    private Long reportId;
}