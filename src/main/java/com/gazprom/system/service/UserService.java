package com.gazprom.system.service;

import com.gazprom.system.payload.*;

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
    boolean approvalOfApplicationByOwner(Long id, Long userId);
    boolean approvalOfApplicationByAdmin(Long id, Long userId);
    boolean rejectionOfRequestByOwner(Long id, Long userId, String reason);
    boolean rejectionOfRequestByAdmin(Long id, Long userId, String reason);
    boolean addSystem(SystemRequest systemRequest);
    boolean addDepartment(String title, Long unitId);
    boolean addPrivilege(String title, String desc);
    boolean addUnit(String title);
    boolean deleteUnit(Long id);
    boolean deleteDepartment(Long id);
    boolean deleteSystem(Long id);
    boolean deletePrivilege(Long id);
    boolean deleteUser(Long id);
    boolean deleteRequest(Long id);
}
