package com.gazprom.system.controller;

import com.gazprom.system.payload.PrivilegeRequest;
import com.gazprom.system.service.PrivilegeService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/privilege")
@AllArgsConstructor
public class PrivilegeController {

  private final PrivilegeService privilegeService;

  @PostMapping()
  public ResponseEntity<?> createPrivilege(@RequestBody PrivilegeRequest privilegeRequest) {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(privilegeService.addPrivilege(privilegeRequest));
  }

  @DeleteMapping()
  public ResponseEntity<?> deletePrivilege(@RequestParam(name = "id") Long privilegeId) {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(privilegeService.deletePrivilege(privilegeId));
  }
}
