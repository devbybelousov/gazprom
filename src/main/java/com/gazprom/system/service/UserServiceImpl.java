package com.gazprom.system.service;

import com.gazprom.system.enumeration.RoleName;
import com.gazprom.system.enumeration.StatusName;
import com.gazprom.system.exceprion.AppException;
import com.gazprom.system.mail.EmailService;
import com.gazprom.system.model.*;
import com.gazprom.system.payload.Date;
import com.gazprom.system.repository.*;
import com.gazprom.system.payload.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private SystemRepository systemRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private EmailService emailService;

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public boolean createUser(UserRequest userRequest) {
        if (userRepository.existsByUserName(userRequest.getUserName())) {
            return false;
        }
        String titleRole = "";
        int index = 0;
        for (RoleName roleName : RoleName.values()){
            if (index == userRequest.getRole()) titleRole = roleName.toString();
            index++;
        }

        if (!roleRepository.existsByRole(titleRole)){
            Role role = new Role(titleRole);
            roleRepository.save(role);
        }

        Role userRole = roleRepository.findByRole(titleRole)
                .orElseThrow(() -> new AppException("User Role not found."));
        logger.debug(userRole.getRole());
        Department department = departmentRepository.findById(userRequest.getDepartmentId())
                .orElseThrow(() -> new AppException(("User Department not found.")));
        logger.debug(department.getTitle());


        User user = new User(userRequest.getUserName(), userRequest.getPassword(),
                userRequest.getName(), userRequest.getLastName(),
                userRequest.getMiddleName(), Collections.singleton(userRole), department);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmail(userRequest.getEmail());


        user.setRoles(Collections.singleton(userRole));
        userRepository.save(user);
        logger.debug("User is create.");
        return true;
    }

    @Override
    public List<?> getAllRequestByStatusEnable(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException("User not found."));
        List<Request> allRequests = user.getRequests();
        List<Request> allActiveRequests = new ArrayList<>();
        for (Request request : allRequests) {
            if (request.getStatus().equals(StatusName.STATUS_ENABLE.toString()))
                allActiveRequests.add(request);
        }
        return getFormatRequest(allActiveRequests);
    }

    @Override
    public SystemResponse getSystemById(Long id){
        InformationSystem system = systemRepository.findById(id).orElseThrow(() -> new AppException("System not found."));
        return new SystemResponse(system.getId(), system.getTitle(), system.getPrivileges(), system.getOwner().getId(), system.getPrimaryAdmin().getId(), system.getBackupAdmin().getId());
    }

    @Override
    public List<?> getAllSystem() {
        List<InformationSystem> systems = systemRepository.findAll();
        List<SystemResponse> formatSystems = new ArrayList<>();
        for (InformationSystem system : systems){
            formatSystems.add(new SystemResponse(system.getId(), system.getTitle(), system.getPrivileges(), system.getOwner().getId(), system.getPrimaryAdmin().getId(), system.getBackupAdmin().getId()));
        }
        return formatSystems;
    }

    @Override
    public List<?> getAllUser() {
        List<User> users = new ArrayList<>();
        for (User user : userRepository.findAll()){
            if (user.getRoles().iterator().next().getRole().equals(RoleName.ROLE_USER.toString()))
                users.add(user);
        }
        return getFormatUsers(users);
    }

    @Override
    public List<?> getAllUserRequest(Long id) {
        User user = userRepository.findById(id).orElseThrow(()->new AppException("User not found."));
        return getFormatRequest(user.getRequests());
    }

    @Override
    public boolean addRequest(ApplicationRequest requestFormat) {
        InformationSystem system = systemRepository.findById(requestFormat.getIdSystem()).orElseThrow(() -> new AppException("System not found."));
        History history = new History("", system.getOwner(), StatusName.STATUS_SHIPPED.toString(), new Timestamp(System.currentTimeMillis()));
        historyRepository.save(history);
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
        historyRepository.save(history);
        sendEmailUsers(request, "html/creating", "Заявка на рассмотрении", "", false);
        sendEmailAdminOrOwner(request, system.getOwner());
        return true;
    }

    @Override
    public UserProfile getUserInfo(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new AppException("User not found."));
        return new UserProfile(id, user.getUserName(), user.getEmail(), user.getName(), user.getLastName(), user.getMiddleName(), user.getDepartment().getTitle());
    }

    @Override
    public List<?> getHistoryByRequest(Long id) {
        Request request = requestRepository.findAllById(id).orElseThrow(()-> new AppException("Request not found."));
        List<HistoryResponse> historyResponses = new ArrayList<>();
        for(History history : request.getHistory()){
            historyResponses.add(new HistoryResponse(history.getUser().getId(), history.getReason(), new Date(history.getDate().getDate(), history.getDate().getMonth(), history.getDate().getYear()), history.getStatus()));
        }
        return historyResponses;
    }

    @Override
    public List<?> getAllDepartmentByUnit(Long id) {
        return departmentRepository.findAllByUnitId(id);
    }

    @Override
    public List<?> getAllUnit() {
        return unitRepository.findAll();
    }

    @Override
    public boolean approvalOfApplicationByOwner(Long id, Long userId) {
        Request request = requestRepository.findAllById(id).orElseThrow(() -> new AppException("Request not found."));
        User owner = request.getInformationSystem().getOwner();
        if (!userId.equals(owner.getId())) return false;
        History historyOwner = new History("", owner, StatusName.STATUS_ENABLE.toString(), new Timestamp(System.currentTimeMillis()));
        History historyAdmin = new History("", request.getInformationSystem().getPrimaryAdmin(), StatusName.STATUS_SHIPPED.toString(), new Timestamp(System.currentTimeMillis()));
        historyRepository.save(historyOwner);
        historyRepository.save(historyAdmin);
        List<History> historyList = request.getHistory();
        historyList.add(historyOwner);
        historyList.add(historyAdmin);
        requestRepository.save(request);
        sendEmailAdminOrOwner(request, request.getInformationSystem().getPrimaryAdmin());
        return true;
    }

    @Override
    public boolean approvalOfApplicationByAdmin(Long id, Long userId) {
        Request request = requestRepository.findAllById(id).orElseThrow(() -> new AppException("Request not found."));
        if (!userId.equals(request.getInformationSystem().getPrimaryAdmin().getId())) return false;
        History history = new History("", request.getInformationSystem().getPrimaryAdmin(), StatusName.STATUS_ENABLE.toString(), new Timestamp(System.currentTimeMillis()));
        historyRepository.save(history);
        List<History> historyList = request.getHistory();
        historyList.add(history);
        request.setExpiryDate(new Timestamp(System.currentTimeMillis()));
        request.setStatus(StatusName.STATUS_ENABLE.toString());
        requestRepository.save(request);
        sendEmailUsers(request, "html/confirmation", "Заявка подтверждена", "", true);
        return true;
    }

    @Override
    public boolean rejectionOfRequestByOwner(Long id, Long userId, String reason) {
        Request request = requestRepository.findAllById(id).orElseThrow(() -> new AppException("Request not found."));
        if (!userId.equals(request.getInformationSystem().getOwner().getId())) return false;
        History history = new History(reason, request.getInformationSystem().getOwner(), StatusName.STATUS_REFUSED.toString(), new Timestamp(System.currentTimeMillis()));
        return refused(reason, request, history);
    }

    @Override
    public boolean rejectionOfRequestByAdmin(Long id, Long userId, String reason) {
        Request request = requestRepository.findAllById(id).orElseThrow(() -> new AppException("Request not found."));
        if (!userId.equals(request.getInformationSystem().getPrimaryAdmin().getId())) return false;
        History history = new History(reason, request.getInformationSystem().getPrimaryAdmin(), StatusName.STATUS_REFUSED.toString(), new Timestamp(System.currentTimeMillis()));
        return refused(reason, request, history);
    }

    private boolean refused(String reason, Request request, History history) {
        historyRepository.save(history);
        List<History> historyList = request.getHistory();
        historyList.add(history);
        request.setStatus(StatusName.STATUS_REFUSED.toString());
        requestRepository.save(request);
        sendEmailUsers(request, "html/refused", "Заявка отклонена", reason, false);
        return true;
    }

    @Override
    public boolean addSystem(SystemRequest systemRequest) {
        User owner = userRepository.findById(systemRequest.getOwnerId()).orElseThrow(() -> new AppException("User not found."));
        User primaryAdmin = userRepository.findById(systemRequest.getPrimaryAdminId()).orElseThrow(() -> new AppException("User not found."));
        User backupAdmin = userRepository.findById(systemRequest.getBackupAdminId()).orElseThrow(() -> new AppException("User not found."));
        List<Privilege> privileges = new ArrayList<>();
        for (Long id : systemRequest.getPrivilegesId()){
            Privilege privilege = privilegeRepository.findById(id).orElseThrow(() -> new AppException("Privilege not found."));
            logger.error(privilege.getTitle());
            privileges.add(privilege);
        }
        InformationSystem system = new InformationSystem(systemRequest.getTitle(), owner, primaryAdmin, backupAdmin, privileges);
        systemRepository.save(system);
        return true;
    }

    @Override
    public boolean addDepartment(String title, Long unitId) {
        Unit unit = unitRepository.findById(unitId).orElseThrow(() -> new AppException("Unit not found."));
        Department department = new Department(title, unit);
        departmentRepository.save(department);
        return true;
    }

    @Override
    public boolean addPrivilege(String title, String desc) {
        Privilege privilege = new Privilege(title, desc);
        privilegeRepository.save(privilege);
        return true;
    }

    @Override
    public boolean addUnit(String title) {
        Unit unit = new Unit(title);
        unitRepository.save(unit);
        return true;
    }

    @Override
    public boolean deleteUnit(Long id) {
        Unit unit = unitRepository.findById(id).orElseThrow(() -> new AppException("Unit not found."));
        unitRepository.delete(unit);
        return true;
    }

    @Override
    public boolean deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new AppException("Department not found."));
        departmentRepository.delete(department);
        return true;
    }

    @Override
    public boolean deleteSystem(Long id) {
        InformationSystem system = systemRepository.findById(id).orElseThrow(() -> new AppException("System not found."));
        systemRepository.delete(system);
        return true;
    }

    @Override
    public boolean deletePrivilege(Long id) {
        Privilege privilege = privilegeRepository.findById(id).orElseThrow(() -> new AppException("Privilege not found."));
        privilegeRepository.delete(privilege);
        return true;
    }

    @Override
    public boolean deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException("User not found."));
        userRepository.delete(user);
        return true;
    }

    @Override
    public boolean deleteRequest(Long id) {
        Request request = requestRepository.findById(id).orElseThrow(() -> new AppException("Request not found."));
        requestRepository.delete(request);
    return true;
    }

    @Override
    public List<?> getAllAdminRequest(Long id) {
        return getAllRequestAdminOrOwner(systemRepository.findAllByPrimaryAdminId(id), id);
    }

    @Override
    public List<?> getAllOwnerRequest(Long id) {
        return getAllRequestAdminOrOwner(systemRepository.findAllByOwnerId(id), id);
    }

    @Override
    public boolean updateUserPasswordOrEmail(String userName, String password, String email) {
        User user = userRepository.findByUserName(userName).orElseThrow(() -> new AppException("User not found."));
        if (!password.equals(""))
            user.setPassword(passwordEncoder.encode(password));
        if (!email.equals(""))
            user.setEmail(email);
        userRepository.save(user);
        return true;
    }

    @Override
    public RequestFormat getRequestInfo(Long id) {
        Request request = requestRepository.findAllById(id).orElseThrow(() -> new AppException("Request not found."));
        Date expiryDate = null;
        if (request.getExpiryDate() == null) expiryDate = new Date(1, 1, 1);
        else expiryDate =  new Date(request.getExpiryDate().getDate(), request.getExpiryDate().getMonth(), request.getExpiryDate().getYear());
        return new RequestFormat(request.getId(), request.getStatus(), getFormatUsers(request.getUsers()), request.getPrivileges(),
                new Date(request.getFilingDate().getDate(), request.getFilingDate().getMonth(), request.getFilingDate().getYear()),
                expiryDate, request.getInformationSystem().getId(), request.getInformationSystem().getTitle());
    }

    private List<RequestFormat> getAllRequestAdminOrOwner(List<InformationSystem> systems, Long id){
        List<Request> requestList = new ArrayList<>();
        for (InformationSystem system : systems){
            List<Request> requests = requestRepository.findAllByInformationSystemId(system.getId());
            for (Request request : requests){
                History history = request.getHistory().get(request.getHistory().size() - 1);
                if (history.getStatus().equals(StatusName.STATUS_SHIPPED.toString()) && history.getUser().getId().equals(id)){
                    requestList.add(request);
                }
            }
        }
        return getFormatRequest(requestList);
    }

    public List<User> getAllUserById(List<Long> ids){
        List<User> users = new ArrayList<>();
        for (Long id : ids){
            users.add(userRepository.findById(id).orElseThrow(() -> new AppException("User not found.")));
        }
        return users;
    }

    public List<Privilege> getAllPrivilegesById(List<Long> ids){
        List<Privilege> users = new ArrayList<>();
        for (Long id : ids){
            users.add(privilegeRepository.findById(id).orElseThrow(() -> new AppException("Privileges not found.")));
        }
        return users;
    }

    private List<RequestFormat> getFormatRequest(List<Request> requestList) {
        List<RequestFormat> requests = new ArrayList<>();

        for (Request request : requestList) {
            Date date = null;
            if (request.getExpiryDate() == null) date = new Date(1, 1, 1);
            else date =  new Date(request.getExpiryDate().getDate(), request.getExpiryDate().getMonth(), request.getExpiryDate().getYear());
            requests.add(new RequestFormat(
                    request.getId(),
                    request.getStatus(),
                    getFormatUsers(request.getUsers()),
                    request.getPrivileges(),
                    new Date(request.getFilingDate().getDate(), request.getFilingDate().getMonth(), request.getFilingDate().getYear()),
                    date,
                    request.getInformationSystem().getId(),
                    request.getInformationSystem().getTitle()
            ));
        }
        return requests;
    }

    private List<UserProfile> getFormatUsers(List<User> users) {
        List<UserProfile> userProfiles = new ArrayList<>();
        for (User user : users) {
            if (user.getDepartment() != null)
                userProfiles.add(new UserProfile(
                        user.getId(),
                        user.getUserName(),
                        user.getEmail(),
                        user.getName(),
                        user.getLastName(),
                        user.getMiddleName(),
                        user.getDepartment().getTitle()
                ));
        }
        return userProfiles;
    }

    private Map<String, Object> getTemplate(Request request){
        java.util.Date dateNow = new java.util.Date(request.getFilingDate().getTime());
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
        StringBuilder roles = new StringBuilder();

        for (Privilege role : request.getInformationSystem().getPrivileges()) {
            roles.append(role.getTitle()).append(", ");
        }

        roles = new StringBuilder(roles.substring(0, roles.length() - 2));

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("user", "");
        templateModel.put("system", request.getInformationSystem().getTitle());
        templateModel.put("roles", roles);
        templateModel.put("dateShipped", formatForDateNow.format(dateNow));
        templateModel.put("admin", false);
        templateModel.put("href", "");
        return templateModel;
    }

    private void sendEmailAdminOrOwner(Request request, User user){
        Map<String, Object> templateModel = getTemplate(request);
        String userName = user.getLastName() + " " + user.getName() + " " + user.getMiddleName();
        templateModel.replace("user", userName);
        templateModel.replace("admin", true);
        templateModel.replace("href", "http://u97708.test-handyhost.ru/admin=" + user.getId() +"/info/" + request.getId());
        sendHtmlMessage(user.getEmail(), "Подтверждение заявки", templateModel, "html/creating");
    }

    private void sendEmailUsers(Request request, String pathToHtml, String subject, String reason, boolean isConfirm) {
        Map<String, Object> templateModel = getTemplate(request);
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
        for (User user : request.getUsers()) {
            String userName = user.getLastName() + " " + user.getName() + " " + user.getMiddleName();
            templateModel.replace("user", userName);
            if (!reason.equals("")) templateModel.put("reason", reason);

            if (isConfirm) templateModel.put("dateExpiry", formatForDateNow.format(new java.util.Date(request.getExpiryDate().getTime())));
            sendHtmlMessage(user.getEmail(), subject, templateModel, pathToHtml);
        }
    }

    private void sendHtmlMessage(String to, String subject, Map<String, Object> template, String pathToHtml) {
        try {
            emailService.sendMessageUsingThymeleafTemplate(to,subject, template, pathToHtml);
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
    }

}
