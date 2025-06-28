package org.example.entities.usergroup;

import jakarta.persistence.*;
import lombok.*;
import org.example.entities.group.Group;
import org.example.entities.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserGroup {

    @EmbeddedId
    private UserGroupId id;

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Group group;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @Column(name = "joined_at", nullable = false)
    @Builder.Default
    private LocalDateTime joinedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "added_by", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User addedBy;
}

