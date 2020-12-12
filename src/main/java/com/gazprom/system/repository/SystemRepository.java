package com.gazprom.system.repository;

import com.gazprom.system.model.InformationSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemRepository extends JpaRepository<InformationSystem, Long> {
    Optional<InformationSystem> findById(Long id);
}
