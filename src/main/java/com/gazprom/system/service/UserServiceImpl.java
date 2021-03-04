package com.gazprom.system.service;

import com.gazprom.system.enumeration.RoleName;
import com.gazprom.system.exceprion.AppException;
import com.gazprom.system.model.Department;
import com.gazprom.system.model.Role;
import com.gazprom.system.model.User;
import com.gazprom.system.payload.JwtAuthenticationResponse;
import com.gazprom.system.payload.LoginRequest;
import com.gazprom.system.payload.UserRequest;
import com.gazprom.system.repository.DepartmentRepository;
import com.gazprom.system.repository.RoleRepository;
import com.gazprom.system.repository.UserRepository;
import com.gazprom.system.security.JwtTokenProvider;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final DepartmentRepository departmentRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider tokenProvider;
  private final AuthenticationManager authenticationManager;

  private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  @Override
  public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest) {
    String username = loginRequest.getUserName();
    String password = loginRequest.getPassword();

    Authentication authentication;
    try {
      authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              username,
              password
          )
      );
    } catch (AuthenticationException e) {
      logger.error("Invalid username/password supplied");
      throw new BadCredentialsException("Invalid username/password supplied");
    }
    SecurityContextHolder.getContext().setAuthentication(authentication);

    User user = userRepository.findByUserName(username)
        .orElseThrow(() ->
            new UsernameNotFoundException("User not found with username: " + username)
        );

    String jwt = tokenProvider.generateToken(authentication);
    return new JwtAuthenticationResponse(jwt, user.getId(),
        user.getRoles().iterator().next().getRole());
  }

  @Override
  public short createUser(UserRequest userRequest) {
    if (userRepository.existsByUserName(userRequest.getUserName())) {
      return -1;
    }

    String titleRole = RoleName.values()[userRequest.getRole()].toString();

    Role userRole = roleRepository.findByRole(titleRole)
        .orElseThrow(() -> new AppException("User Role not found."));
    Department department = departmentRepository.findById(userRequest.getDepartmentId())
        .orElseThrow(() -> new AppException(("User Department not found.")));

    User user = new User(userRequest.getUserName(), userRequest.getPassword(),
        userRequest.getName(), userRequest.getLastName(),
        userRequest.getMiddleName(), Collections.singleton(userRole), department);

    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setEmail(userRequest.getEmail());

    user.setRoles(Collections.singleton(userRole));
    userRepository.save(user);
    logger.debug("User is create.");
    return 1;
  }

  @Override
  public short updateUser(UserRequest user) {
    User newUser = userRepository.findByUserName(user.getUserName())
        .orElseThrow(() -> new AppException("User not found."));

    String titleRole = RoleName.values()[user.getRole()].toString();
    Role role = roleRepository.findByRole(titleRole)
        .orElseThrow(() -> new AppException("Role not found."));
    Department department = departmentRepository.findById(user.getDepartmentId())
        .orElseThrow(() -> new AppException("Department not found."));
    newUser.getRoles().add(role);
    newUser.setPassword(passwordEncoder.encode(user.getPassword()));
    newUser.setEmail(user.getEmail());
    newUser.setName(user.getName());
    newUser.setLastName(user.getLastName());
    newUser.setMiddleName(user.getMiddleName());
    newUser.setDepartment(department);
    userRepository.save(newUser);
    return 1;
  }

  @Override
  public List<?> getAllUser() {
    return userRepository.findAll();
  }

  @Override
  public User getUserInfo(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new AppException("User not found."));
  }

  @Override
  public boolean isExistUser(String userName) {
    return userRepository.existsByUserName(userName);
  }


  @Override
  public short deleteUser(Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new AppException("User not found."));
    userRepository.delete(user);
    return 1;
  }

  @Override
  public List<?> getAllRoles() {
    return roleRepository.findAll();
  }
}
