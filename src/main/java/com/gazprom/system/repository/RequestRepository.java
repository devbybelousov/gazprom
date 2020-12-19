package com.gazprom.system.repository;

import com.gazprom.system.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByStatus(String status);
    Optional<Request> findAllById(Long id);
    List<Request> findAllByInformationSystemId(Long id);
}
