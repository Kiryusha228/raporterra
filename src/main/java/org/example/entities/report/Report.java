package org.example.entities.report;

import jakarta.persistence.*;
import lombok.*;
import org.example.entities.collection.CollectionReport;
import org.example.entities.dbconnection.DatabaseConnection;
import org.example.entities.user.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

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
    private User createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User updatedBy;

    @Column(name = "is_public", nullable = false)
    @Builder.Default
    private boolean isPublic = false;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<ReportParameter> parameters = new HashSet<>();

    @OneToMany(mappedBy = "report")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<CollectionReport> collectionReports = new HashSet<>();
}
