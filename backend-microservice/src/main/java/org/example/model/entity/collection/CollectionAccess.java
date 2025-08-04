package org.example.model.entity.collection;
import jakarta.persistence.*;
import lombok.*;
import org.example.model.entity.group.Group;
import org.example.model.entity.user.UserInfo;

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
    private UserInfo user;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Group group;

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
