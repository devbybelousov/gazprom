package com.gazprom.system.repository;

import com.gazprom.system.model.InformationSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemRepository extends JpaRepository<InformationSystem, Long> {
}
