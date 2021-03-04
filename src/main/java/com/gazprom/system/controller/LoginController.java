package com.gazprom.system.controller;

import com.gazprom.system.payload.LoginRequest;
import com.gazprom.system.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class LoginController {

  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(userService.authenticateUser(loginRequest));
  }


}
