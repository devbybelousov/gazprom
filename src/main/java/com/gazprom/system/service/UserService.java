package com.gazprom.system.service;

import com.gazprom.system.payload.ApplicationRequest;
import com.gazprom.system.payload.SystemResponse;
import com.gazprom.system.payload.UserProfile;
import com.gazprom.system.payload.UserRequest;

import java.util.List;

public interface UserService {
    boolean createUser(UserRequest userRequest);
    List<?> getAllRequestByStatusEnable(Long id);
    SystemResponse getSystemById(Long id);
    List<?> getAllSystem();
    List<?> getAllUser();
    List<?> getAllUserRequest(Long id);
    boolean addRequest(ApplicationRequest requestFormat);
    UserProfile getUserInfo(Long id);
    List<?> getHistoryByRequest(Long id);
    List<?> getAllDepartmentByUnit(Long id);
    List<?> getAllUnit();
    boolean approvalOfApplicationByOwner(Long id);
    boolean approvalOfApplicationByAdmin(Long id);
    boolean rejectionOfRequestByOwner(Long id);
    boolean rejectionOfRequestByAdmin(Long id);
    boolean setActiveRequest(Long id);
}
