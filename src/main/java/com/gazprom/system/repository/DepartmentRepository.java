package com.gazprom.system.repository;

import com.gazprom.system.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findAllByUnitId(Long id);
    Optional<Department> findById(Long id);
}
