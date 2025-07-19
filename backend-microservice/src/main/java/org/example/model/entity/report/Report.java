package org.example.model.entity.report;

import jakarta.persistence.*;
import lombok.*;
import org.example.model.entity.collection.CollectionReport;
import org.example.model.entity.dbconnection.DatabaseConnection;
import org.example.model.entity.user.UserInfo;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "report_id")
    private UUID id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column
    private String description;

    @Column(name = "sql_query", nullable = false)
    private String sqlQuery;

    @ManyToOne
    @JoinColumn(name = "connection_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private DatabaseConnection connection;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private UserInfo createdBy;

    @Column(name = "is_public", nullable = false)
    @Builder.Default
    private boolean isPublic = false;

    @OneToMany(mappedBy = "report")
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<CollectionReport> collectionReports = new HashSet<>();
}
