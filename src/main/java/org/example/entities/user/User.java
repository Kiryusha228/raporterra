package org.example.entities.user;

import jakarta.persistence.*;
import lombok.*;
import org.example.entities.collection.Collection;
import org.example.entities.collection.CollectionAccess;
import org.example.entities.dbconnection.DatabaseConnection;
import org.example.entities.group.Group;
import org.example.entities.report.Report;
import org.example.entities.usergroup.UserGroup;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String password;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean isActive = true;

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

    @Override
    public java.util.Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
}
