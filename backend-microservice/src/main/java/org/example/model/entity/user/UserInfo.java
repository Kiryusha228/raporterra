package org.example.model.entity.user;

import jakarta.persistence.*;
import lombok.*;
import org.example.model.entity.collection.Collection;
import org.example.model.entity.collection.CollectionAccess;
import org.example.model.entity.dbconnection.DatabaseConnection;
import org.example.model.entity.group.Group;
import org.example.model.entity.report.Report;
import org.example.model.entity.usergroup.UserGroup;

import java.util.HashSet;
import java.util.Set;


@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_info")
public class UserInfo {

    @Id
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, unique = true, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @OneToMany(mappedBy = "createdBy")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Group> createdGroups = new HashSet<>();

    @OneToMany(mappedBy = "createdBy")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<DatabaseConnection> createdConnections = new HashSet<>();

    @OneToMany(mappedBy = "createdBy")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Report> createdReports = new HashSet<>();

    @OneToMany(mappedBy = "updatedBy")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Report> updatedReports = new HashSet<>();

    @OneToMany(mappedBy = "createdBy")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Collection> createdCollections = new HashSet<>();

    @OneToMany(mappedBy = "grantedBy")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<CollectionAccess> grantedAccesses = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<UserGroup> userGroups = new HashSet<>();
}
