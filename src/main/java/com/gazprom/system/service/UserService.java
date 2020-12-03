package com.gazprom.system.service;

import com.gazprom.system.exceprion.AppException;
import com.gazprom.system.model.Department;
import com.gazprom.system.model.Role;
import com.gazprom.system.model.User;
import com.gazprom.system.repository.*;
import com.gazprom.system.payload.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    SystemRepository systemRepository;

    @Autowired
    UnitRepository unitRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public boolean createUser(UserRequest userRequest){
        if (userRepository.existsByUserName(userRequest.getUserName())) {
            return false;
        }

        Role userRole = roleRepository.findById(userRequest.getRoleId())
                .orElseThrow(() -> new AppException("User Role not set."));
        Department department = departmentRepository.findById(userRequest.getDepartmentId())
                .orElseThrow(() -> new AppException(("User Department not set.")));


        User user = new User(userRequest.getUserName(), userRequest.getPassword(),
                userRequest.getName(), userRequest.getLastName(),
                userRequest.getMiddleName(), Collections.singleton(userRole), department);

        user.setPassword(passwordEncoder.encode(user.getPassword()));


        user.setRoles(Collections.singleton(userRole));
        userRepository.save(user);
        logger.debug("User is create.");
        return true;
    }
}
