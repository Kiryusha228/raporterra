package org.example.repository;

import org.example.model.entity.collection.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
    @Override
    Optional<Collection> findById(Long id);
}
