package org.example.repository;

import org.example.model.entity.usergroup.UserGroup;
import org.example.model.entity.usergroup.UserGroupId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserGroupRepository extends JpaRepository<UserGroup, UserGroupId>{
    @Override
    Optional<UserGroup> findById(UserGroupId id);
}
