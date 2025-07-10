package org.example.model.entity.dbconnection;

import jakarta.persistence.*;
import lombok.*;
import org.example.model.entity.report.Report;
import org.example.model.entity.user.UserInfo;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "database_connections")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatabaseConnection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "connection_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "db_type", nullable = false, length = 50)
    private String dbType;

    @Column(nullable = false, length = 255)
    private String host;

    @Column(nullable = false)
    private Integer port;

    @Column(name = "database_name", nullable = false, length = 100)
    private String databaseName;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(name = "encrypted_password", nullable = false)
    private String encryptedPassword;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private UserInfo createdBy;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean isActive = true;

    @OneToMany(mappedBy = "connection")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Report> reports = new HashSet<>();
}
