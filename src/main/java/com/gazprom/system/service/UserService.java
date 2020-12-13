package com.gazprom.system.service;

import com.gazprom.system.enumeration.StatusName;
import com.gazprom.system.exceprion.AppException;
import com.gazprom.system.model.*;
import com.gazprom.system.repository.*;
import com.gazprom.system.payload.*;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {

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
    private JavaMailSender emailSender;

    @Autowired
    private HistoryRepository historyRepository;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public boolean createUser(UserRequest userRequest) {
        if (userRepository.existsByUserName(userRequest.getUserName())) {
            return false;
        }

        Role userRole = roleRepository.findById(4L)
                .orElseThrow(() -> new AppException("User Role not set."));
        Department department = departmentRepository.findById(userRequest.getDepartmentId())
                .orElseThrow(() -> new AppException(("User Department not set.")));


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

    public List<?> getAllActiveRequest(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        List<Request> allRequests = user.getRequests();
        List<Request> allActiveRequests = new ArrayList<>();
        for (Request request : allRequests) {
            if (request.getStatus().equals(StatusName.STATUS_ENABLE))
                allActiveRequests.add(request);
        }
        return getFormatRequest(allActiveRequests);
    }

    public List<?> getAllSystem() {
        return systemRepository.findAll();
    }

    public List<?> getAllUser() {
        List<User> users = userRepository.findAll();
        return getFormatUsers(users);
    }

    public List<?> getAllUserRequest(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return getFormatRequest(user.getRequests());
    }

    public boolean addRequest(ApplicationRequest requestFormat) {
        InformationSystem system = systemRepository.findById(requestFormat.getIdSystem()).orElseThrow();
        History history = new History("", system.getOwner());
        historyRepository.save(history);
        List<History> historyList = new ArrayList<>();
        historyList.add(history);
        Request request = new Request(StatusName.STATUS_SHIPPED.toString(),
                Timestamp.valueOf(
                        requestFormat.getFillingDate().getYear() + "-" +
                                requestFormat.getFillingDate().getMonth() + "-" +
                                requestFormat.getFillingDate().getDay() + " 00:00:00.0"),
                Timestamp.valueOf(
                        requestFormat.getExpiryDate().getYear() + "-" +
                                requestFormat.getExpiryDate().getMonth() + "-" +
                                requestFormat.getExpiryDate().getDay() + " 00:00:00.0"),
                getAllUserById(requestFormat.getUsersId()),
                historyList,
                system
        );
        sendEmailAddRequest(request);
        return true;
    }

    public List<User> getAllUserById(List<Long> ids){
        List<User> users = new ArrayList<>();
        for (Long id : ids){
            users.add(userRepository.findById(id).orElseThrow());
        }
        return users;
    }
    
    public UserProfile getUserInfo(Long id){
        User user = userRepository.findById(id).orElseThrow();
        return new UserProfile(id, user.getUserName(), user.getEmail(), user.getName(), user.getLastName(), user.getMiddleName(), user.getDepartment().getTitle());
    }

    public List<RequestFormat> getFormatRequest(List<Request> requestList) {
        List<RequestFormat> requests = new ArrayList<>();
        for (Request request : requestList) {
            requests.add(new RequestFormat(
                    request.getId(),
                    request.getStatus(),
                    request.getHistory(),
                    getFormatUsers(request.getUsers()),
                    new Date(request.getFilingDate().getDate(), request.getFilingDate().getMonth(), request.getFilingDate().getYear()),
                    new Date(request.getExpiryDate().getDate(), request.getExpiryDate().getMonth(), request.getExpiryDate().getYear()),
                    request.getInformationSystem().getId()
            ));
        }
        return requests;
    }

    public List<UserProfile> getFormatUsers(List<User> users) {
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

    public void sendEmailAddRequest(Request request) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        java.util.Date dateNow = new java.util.Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
        StringBuilder roles = new StringBuilder();
        for (Privilege role : request.getInformationSystem().getPrivileges()) {
            roles.append(role.getTitle()).append(", ");
        }
        roles.deleteCharAt(roles.length() - 1);
        for (User user : request.getUsers()) {
            try {
                String htmlMsg = "<div class=\"container\" style=\"max-width: 90%; margin: 0 auto;\">" +
                        "<header style=\"display: flex; margin-bottom: 10px\">" +
                        "<img src=\"gazprom-logo.svg\" style=\"width: 94px; height: 151px; margin: 0 48px 0 0\" alt=\"placeholder+image\">" +
                        "<div class=\"header_text\" style=\"color: #262626;font-size: 64px; padding:64px 0 0 0;\">" +
                        "Здравствуйте, " + user.getName() + " " + user.getLastName() + " " + user.getMiddleName() +
                        "</div>" +
                        "</header>" +
                        "<main style=\"margin: 0; padding: 0 20px; background-color: #E2F1F8\">" +
                        "<p style=\"color: #000;font-size: 48px;margin: 0; padding: 20px 0;\">Ваша заявка отправлена на рассмотрение</p>" +
                        "<p style=\"color: #000;font-size: 48px;margin: 0; padding-bottom: 20px;\">Информационная система: " + request.getInformationSystem().getTitle() + " </p>" +
                        "<p style=\"color: #000;font-size: 48px;margin: 0; padding-bottom: 20px;\">Роли: " + roles + "</p>" +
                        "<p style=\"color: #000;font-size: 48px;margin: 0; padding-bottom: 20px;\">Дата подачи: " + formatForDateNow.format(dateNow) + "</p>" +
                        "</main>" +
                        "</div>";
                helper = new MimeMessageHelper(message, true, "utf-8");
                message.setHeader("Content-Type", "text/html");
                message.setContent(htmlMsg, "text/html");
                helper.setTo(user.getEmail());
                helper.setSubject("Заявка на рассмотрении");
                this.emailSender.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        try {
            String htmlMsg = "<div class=\"container\" style=\"max-width: 90%; margin: 0 auto;\">" +
                    "<header style=\"display: flex; margin-bottom: 10px\">" +
                    "<img src=\"gazprom-logo.svg\" style=\"width: 94px; height: 151px; margin: 0 48px 0 0\" alt=\"placeholder+image\">" +
                    "<div class=\"header_text\" style=\"color: #262626;font-size: 64px; padding:64px 0 0 0;\">" +
                    "Здравствуйте, " + request.getInformationSystem().getOwner().getName() + " " +
                    request.getInformationSystem().getOwner().getLastName() + " " +
                    request.getInformationSystem().getOwner().getMiddleName() +
                    "</div>" +
                    "</header>" +
                    "<main style=\"margin: 0; padding: 0 20px; background-color: #E2F1F8\">" +
                    "<p style=\"color: #000;font-size: 48px;margin: 0; padding: 20px 0;\">Отправлена заявка на рассмотрении</p>" +
                    "<p style=\"color: #000;font-size: 48px;margin: 0; padding-bottom: 20px;\">Информационная система: " + request.getInformationSystem().getTitle() + " </p>" +
                    "<p style=\"color: #000;font-size: 48px;margin: 0; padding-bottom: 20px;\">Роли: " + roles + "</p>" +
                    "<p style=\"color: #000;font-size: 48px;margin: 0; padding-bottom: 20px;\">Дата подачи: " + formatForDateNow.format(dateNow) + "</p>" +
                    "<p style=\"color: #000;font-size: 48px;margin: 0; padding-bottom: 20px;\">Для принятия или отклонения заявки перейдите по ссылке: http://localhost:3000 " + formatForDateNow.format(dateNow) + "</p>" +
                    "</main>" +
                    "</div>";
            helper = new MimeMessageHelper(message, true, "utf-8");
            message.setHeader("Content-Type", "text/html");
            message.setContent(htmlMsg, "text/html");
            helper.setTo(request.getInformationSystem().getOwner().getEmail());
            helper.setSubject("Заявка на рассмотрении");
            this.emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
