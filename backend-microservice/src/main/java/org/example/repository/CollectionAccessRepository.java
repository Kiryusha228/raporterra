package org.example.repository;

import org.example.model.entity.collection.CollectionAccess;
import org.example.model.entity.group.Group;
import org.example.model.entity.user.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CollectionAccessRepository extends JpaRepository<CollectionAccess, Long> {
    @Override
    Optional<CollectionAccess> findById(Long id);

    List<CollectionAccess> findByUser(UserInfo user);
    List<CollectionAccess> findByGroup(Group group);
}
