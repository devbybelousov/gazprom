package com.gazprom.system.service;

import com.gazprom.system.enumeration.StatusName;
import com.gazprom.system.exceprion.AppException;
import com.gazprom.system.mail.EmailService;
import com.gazprom.system.model.History;
import com.gazprom.system.model.InformationSystem;
import com.gazprom.system.model.Privilege;
import com.gazprom.system.model.Request;
import com.gazprom.system.model.User;
import com.gazprom.system.payload.ApplicationRequest;
import com.gazprom.system.repository.HistoryRepository;
import com.gazprom.system.repository.PrivilegeRepository;
import com.gazprom.system.repository.RequestRepository;
import com.gazprom.system.repository.SystemRepository;
import com.gazprom.system.repository.UserRepository;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {

  private final UserRepository userRepository;
  private final RequestRepository requestRepository;
  private final SystemRepository systemRepository;
  private final HistoryRepository historyRepository;
  private final PrivilegeRepository privilegeRepository;
  private final EmailService emailService;

  @Override
  public short addRequest(ApplicationRequest requestFormat) {
    InformationSystem system = systemRepository.findById(requestFormat.getSystemId())
        .orElseThrow(() -> new AppException("System not found."));
    History history = new History("", system.getOwner(), StatusName.STATUS_SHIPPED.toString(),
        new Timestamp(System.currentTimeMillis()));
    List<History> historyList = new ArrayList<>();
    historyList.add(history);
    Request request = new Request(StatusName.STATUS_SHIPPED.toString(),
        new Timestamp(System.currentTimeMillis()),
        getAllPrivilegesById(requestFormat.getPrivilegesId()),
        getAllUserById(requestFormat.getUsersId()),
        historyList,
        system
    );
    request = requestRepository.save(request);
    history.setRequest(request);
    emailService.sendEmailToUsers(request, "html/creating", "Заявка на рассмотрении", "", false);
    emailService.sendEmailToAdmin(request, system.getOwner(), false);
    return 1;
  }

  @Override
  public Request getRequestInfo(Long id) {
    return requestRepository.findAllById(id)
        .orElseThrow(() -> new AppException("Request not found."));
  }

  @Override
  public List<?> getAllUserRequest(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new AppException("User not found."));
    return user.getRequests();
  }

  @Override
  public List<?> getAllRequestByStatus(Long userId, String status) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new AppException("User not found."));
    return requestRepository.findAllByUsersAndStatus(user, status);
  }

  @Override
  public List<?> getAllRequestByDate(Long userId, String date) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new AppException("User not found."));
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date newDate = null;
    try {
      newDate = dateFormat.parse(date);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    assert newDate != null;
    return requestRepository.findAllByUsersAndExpiryDateGreaterThanAndFilingDateLessThan(user,
        new Timestamp(newDate.getTime()), new Timestamp(newDate.getTime()));
  }

  @Override
  public List<?> getAllRequestBySystem(Long userId, Long systemId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new AppException("User not found."));
    InformationSystem system = systemRepository.findById(systemId)
        .orElseThrow(() -> new AppException("System not found."));
    return requestRepository.findAllByUsersAndInformationSystem(user, system);
  }

  @Override
  public List<?> getAllAdminRequest(Long id) {
    return getAllRequestAdminOrOwner(systemRepository.findAllByPrimaryAdminId(id), id);
  }

  @Override
  public List<?> getAllOwnerRequest(Long id) {
    return getAllRequestAdminOrOwner(systemRepository.findAllByOwnerId(id), id);
  }

  private List<Request> getAllRequestAdminOrOwner(List<InformationSystem> systems, Long id) {
    List<Request> requestList = new ArrayList<>();
    for (InformationSystem system : systems) {
      List<Request> requests = requestRepository.findAllByInformationSystem(system);
      for (Request request : requests) {
        if (request.getHistory().size() >= 1) {
          History history = request.getHistory().get(request.getHistory().size() - 1);
          if (history.getStatus().equals(StatusName.STATUS_SHIPPED.toString()) && history.getUser()
              .getId().equals(id)) {
            requestList.add(request);
          }
        }
      }
    }
    return requestList;
  }

  @Override
  public short approvalOfApplicationByOwner(Long id, Long userId) {
    Request request = requestRepository.findAllById(id)
        .orElseThrow(() -> new AppException("Request not found."));
    User owner = request.getInformationSystem().getOwner();
    if (!userId.equals(owner.getId())) {
      return -2;
    }
    History historyOwner = new History("", owner, StatusName.STATUS_ENABLE.toString(),
        new Timestamp(System.currentTimeMillis()));
    History historyAdmin = new History("", request.getInformationSystem().getPrimaryAdmin(),
        StatusName.STATUS_SHIPPED.toString(), new Timestamp(System.currentTimeMillis()));
    historyRepository.save(historyOwner);
    historyRepository.save(historyAdmin);
    List<History> historyList = request.getHistory();
    historyList.add(historyOwner);
    historyList.add(historyAdmin);
    requestRepository.save(request);
    emailService.sendEmailToAdmin(request, request.getInformationSystem().getPrimaryAdmin(), false);
    return 1;
  }

  @Override
  public short approvalOfApplicationByAdmin(Long id, Long userId) {
    Request request = requestRepository.findAllById(id)
        .orElseThrow(() -> new AppException("Request not found."));
    if (!userId.equals(request.getInformationSystem().getPrimaryAdmin().getId())) {
      return -2;
    }
    History history = new History("", request.getInformationSystem().getPrimaryAdmin(),
        StatusName.STATUS_ENABLE.toString(), new Timestamp(System.currentTimeMillis()));
    historyRepository.save(history);
    List<History> historyList = request.getHistory();
    historyList.add(history);
    request.setExpiryDate(new Timestamp(System.currentTimeMillis()));
    request.setStatus(StatusName.STATUS_ENABLE.toString());
    requestRepository.save(request);
    emailService.sendEmailToUsers(request, "html/confirmation", "Заявка подтверждена", "", true);
    return 1;
  }

  @Override
  public short rejectionOfRequestByOwner(Long id, Long userId, String reason) {
    Request request = requestRepository.findAllById(id)
        .orElseThrow(() -> new AppException("Request not found."));
    if (!userId.equals(request.getInformationSystem().getOwner().getId())) {
      return -2;
    }
    History history = new History(reason, request.getInformationSystem().getOwner(),
        StatusName.STATUS_REFUSED.toString(), new Timestamp(System.currentTimeMillis()));
    return refused(reason, request, history);
  }

  @Override
  public short rejectionOfRequestByAdmin(Long id, Long userId, String reason) {
    Request request = requestRepository.findAllById(id)
        .orElseThrow(() -> new AppException("Request not found."));
    if (!userId.equals(request.getInformationSystem().getPrimaryAdmin().getId())) {
      return -2;
    }
    History history = new History(reason, request.getInformationSystem().getPrimaryAdmin(),
        StatusName.STATUS_REFUSED.toString(), new Timestamp(System.currentTimeMillis()));
    return refused(reason, request, history);
  }

  private short refused(String reason, Request request, History history) {
    historyRepository.save(history);
    List<History> historyList = request.getHistory();
    historyList.add(history);
    request.setStatus(StatusName.STATUS_REFUSED.toString());
    request = requestRepository.save(request);
    emailService.sendEmailToUsers(request, "html/refused", "Заявка отклонена", reason, false);
    return 1;
  }

  @Override
  public List<?> getHistoryByRequest(Long id) {
    Request request = requestRepository.findAllById(id)
        .orElseThrow(() -> new AppException("Request not found."));
    return request.getHistory();
  }

  @Override
  public short deleteRequest(Long id) {
    Request request = requestRepository.findById(id)
        .orElseThrow(() -> new AppException("Request not found."));
    requestRepository.delete(request);
    return 1;
  }

  public List<User> getAllUserById(List<Long> ids) {
    List<User> users = new ArrayList<>();
    for (Long id : ids) {
      users.add(userRepository.findById(id).orElseThrow(() -> new AppException("User not found.")));
    }
    return users;
  }

  public List<Privilege> getAllPrivilegesById(List<Long> ids) {
    List<Privilege> users = new ArrayList<>();
    for (Long id : ids) {
      users.add(privilegeRepository.findById(id)
          .orElseThrow(() -> new AppException("Privileges not found.")));
    }
    return users;
  }
}
