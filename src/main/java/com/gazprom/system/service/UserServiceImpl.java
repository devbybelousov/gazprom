package com.gazprom.system.service;

import com.gazprom.system.enumeration.StatusName;
import com.gazprom.system.exceprion.AppException;
import com.gazprom.system.mail.EmailService;
import com.gazprom.system.model.*;
import com.gazprom.system.payload.Date;
import com.gazprom.system.repository.*;
import com.gazprom.system.payload.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
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

        Role userRole = roleRepository.findByRole("ROLE_USER")
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
        return new SystemResponse(system.getTitle(), system.getPrivileges(), system.getOwner().getId(), system.getPrimaryAdmin().getId(), system.getBackupAdmin().getId());
    }

    @Override
    public List<?> getAllSystem() {
        List<InformationSystem> systems = systemRepository.findAll();
        List<SystemResponse> formatSystems = new ArrayList<>();
        for (InformationSystem system : systems){
            formatSystems.add(new SystemResponse(system.getTitle(), system.getPrivileges(), system.getOwner().getId(), system.getPrimaryAdmin().getId(), system.getBackupAdmin().getId()));
        }
        return formatSystems;
    }

    @Override
    public List<?> getAllUser() {
        List<User> users = userRepository.findAll();
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
        sendEmailAddRequest(request);
        return true;
    }

    @Override
    public UserProfile getUserInfo(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new AppException("User not found."));
        return new UserProfile(id, user.getUserName(), user.getEmail(), user.getName(), user.getLastName(), user.getMiddleName(), user.getDepartment().getTitle());
    }

    @Override
    public List<?> getHistoryByRequest(Long id) {
        Request request = requestRepository.getById(id).orElseThrow(()-> new AppException("Request not found."));
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
    public boolean approvalOfApplicationByOwner(Long id) {
        return false;
    }

    @Override
    public boolean approvalOfApplicationByAdmin(Long id) {
        return false;
    }

    @Override
    public boolean rejectionOfRequestByOwner(Long id) {
        return false;
    }

    @Override
    public boolean rejectionOfRequestByAdmin(Long id) {
        return false;
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
            requests.add(new RequestFormat(
                    request.getId(),
                    request.getStatus(),
                    request.getHistory(),
                    getFormatUsers(request.getUsers()),
                    request.getPrivileges(),
                    new Date(request.getFilingDate().getDate(), request.getFilingDate().getMonth(), request.getFilingDate().getYear()),
                    new Date(request.getExpiryDate().getDate(), request.getExpiryDate().getMonth(), request.getExpiryDate().getYear()),
                    request.getInformationSystem().getId(),
                    request.getInformationSystem().getTitle()
            ));
        }
        return requests;
    }

    private List<UserProfile> getFormatUsers(List<User> users) {
        List<UserProfile> userProfiles = new ArrayList<>();
        for (User user : users) {
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

    private void sendEmailAddRequest(Request request) {
        java.util.Date dateNow = new java.util.Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
        StringBuilder roles = new StringBuilder();

        for (Privilege role : request.getInformationSystem().getPrivileges()) {
            roles.append(role.getTitle()).append(", ");
        }
        roles.deleteCharAt(roles.length() - 1);

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("user", "");
        templateModel.put("system", request.getInformationSystem().getTitle());
        templateModel.put("roles", roles);
        templateModel.put("dateShipped", formatForDateNow.format(dateNow));
        templateModel.put("owner", false);

        for (User user : request.getUsers()) {
            String userName = user.getName() + " " + user.getLastName() + " " + user.getMiddleName();
            templateModel.replace("user", userName);
            sendHtmlMessage(user.getEmail(), "Заявка на рассмотрении", templateModel, "creatingRequest");
        }
        String userName = request.getInformationSystem().getOwner().getName() + " " +
                request.getInformationSystem().getOwner().getLastName() + " " +
                request.getInformationSystem().getOwner().getMiddleName();
        templateModel.replace("user", userName);
        templateModel.replace("owner", true);
        sendHtmlMessage(request.getInformationSystem().getOwner().getEmail(), "Заявка на рассмотрении", templateModel, "creatingRequest");
    }

    private void sendHtmlMessage(String to, String subject, Map<String, Object> template, String pathToHtml) {
        try {
            emailService.sendMessageUsingThymeleafTemplate(to,subject, template, pathToHtml);
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
    }

}
