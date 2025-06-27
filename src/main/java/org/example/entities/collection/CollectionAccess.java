package org.example.entities.collection;
import jakarta.persistence.*;
import lombok.*;
import org.example.entities.group.Group;
import org.example.entities.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "collection_access")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectionAccess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "access_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "collection_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Collection collection;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Group group;

    @Column(name = "access_level", nullable = false, length = 20)
    private String accessLevel;

    @Column(name = "granted_at", nullable = false)
    @Builder.Default
    private LocalDateTime grantedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "granted_by", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User grantedBy;

    @PrePersist
    @PreUpdate
    private void validate() {
        if (user == null && group == null) {
            throw new IllegalStateException("Either user or group must be set");
        }
        if (user != null && group != null) {
            throw new IllegalStateException("Cannot set both user and group");
        }
    }
}
