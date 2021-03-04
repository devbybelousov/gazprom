package com.gazprom.system.service;

import com.gazprom.system.model.Request;
import com.gazprom.system.payload.ApplicationRequest;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public interface RequestService {

  short addRequest(ApplicationRequest requestFormat);

  Request getRequestInfo(Long id);

  List<?> getAllUserRequest(Long userId);

  List<?> getAllRequestByStatus(Long userId, String status);

  List<?> getAllRequestByDate(Long userId, String date);

  List<?> getAllRequestBySystem(Long userId, Long systemId);

  List<?> getAllAdminRequest(Long id);

  List<?> getAllOwnerRequest(Long id);

  short approvalOfApplicationByOwner(Long id, Long userId);

  short approvalOfApplicationByAdmin(Long id, Long userId);

  short rejectionOfRequestByOwner(Long id, Long userId, String reason);

  short rejectionOfRequestByAdmin(Long id, Long userId, String reason);

  List<?> getHistoryByRequest(Long id);

  short deleteRequest(Long id);
}
