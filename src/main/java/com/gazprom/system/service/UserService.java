package com.gazprom.system.service;

import com.gazprom.system.model.User;
import com.gazprom.system.payload.JwtAuthenticationResponse;
import com.gazprom.system.payload.LoginRequest;
import com.gazprom.system.payload.UserRequest;
import java.util.List;

public interface UserService {

  JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest);

  short createUser(UserRequest userRequest);

  short updateUser(UserRequest user);

  List<?> getAllUser();

  User getUserInfo(Long id);

  boolean isExistUser(String userName);

  short deleteUser(Long id);

  List<?> getAllRoles();

}
