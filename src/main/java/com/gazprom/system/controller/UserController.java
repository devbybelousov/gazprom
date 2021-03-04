package com.gazprom.system.controller;

import com.gazprom.system.payload.UserRequest;
import com.gazprom.system.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping()
  public ResponseEntity<?> createUser(@RequestBody UserRequest signUpRequest) {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(userService.createUser(signUpRequest));
  }

  @GetMapping()
  public ResponseEntity<?> getAllUser() {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(userService.getAllUser());
  }

  @GetMapping("/info")
  public ResponseEntity<?> getUserInfo(@RequestParam(name = "id") Long id) {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(userService.getUserInfo(id));
  }

  @GetMapping("/exists")
  public ResponseEntity<?> isUserNameExists(@RequestParam("userName") String userName) {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(userService.isExistUser(userName));
  }

  @DeleteMapping()
  public ResponseEntity<?> deleteUser(@RequestParam(name = "id") Long userId) {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(userService.deleteUser(userId));
  }

  @PutMapping()
  public ResponseEntity<?> updateUser(@RequestBody UserRequest userRequest) {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(userService.updateUser(userRequest));
  }

  @GetMapping("/roles")
  public ResponseEntity<?> getAllRoles() {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(userService.getAllRoles());
  }
}
