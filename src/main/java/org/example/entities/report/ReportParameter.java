package org.example.entities.report;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "report_parameters")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportParameter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parameter_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false, columnDefinition = "UUID")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Report report;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "param_type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ParameterType paramType;

    @Column(name = "default_value")
    private String defaultValue;

    @Column(name = "is_required", nullable = false)
    @Builder.Default
    private boolean isRequired = false;
}

