package com.gazprom.system.repository;

import com.gazprom.system.model.InformationSystem;
import com.gazprom.system.model.Request;
import com.gazprom.system.model.User;
import java.sql.Time;
import java.sql.Timestamp;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByStatus(String status);
    Optional<Request> findAllById(Long id);
    List<Request> findAllByInformationSystem(InformationSystem id);
    List<Request> findAllByUsersAndStatus(User user, String status);
    List<Request> findAllByUsersAndExpiryDateGreaterThanAndFilingDateLessThan(User user, Timestamp date1, Timestamp date2);
    List<Request> findAllByUsersAndInformationSystem(User user, InformationSystem system);
}
