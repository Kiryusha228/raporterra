package org.example.entities.collection;

import jakarta.persistence.*;
import lombok.*;
import org.example.entities.user.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "collections")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collection_id")
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column
    private String description;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User createdBy;

    @OneToMany(mappedBy = "collection")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<CollectionReport> collectionReports = new HashSet<>();

    @OneToMany(mappedBy = "collection")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<CollectionAccess> collectionAccesses = new HashSet<>();
}