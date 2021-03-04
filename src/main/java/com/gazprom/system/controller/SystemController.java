package com.gazprom.system.controller;

import com.gazprom.system.payload.SystemRequest;
import com.gazprom.system.service.SystemService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system")
@AllArgsConstructor
public class SystemController {

  private final SystemService systemService ;

  @GetMapping("/info")
  public ResponseEntity<?> getSystemInfo(@RequestParam(name = "id") Long id) {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(systemService.getSystemInfoById(id));
  }

  @GetMapping()
  public ResponseEntity<?> getAllSystem() {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(systemService.getAllSystem());
  }

  @PostMapping()
  public ResponseEntity<?> createSystem(@RequestBody SystemRequest systemRequest) {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(systemService.addSystem(systemRequest));
  }

  @DeleteMapping()
  public ResponseEntity<?> deleteSystem(@RequestParam("id") Long systemId) {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(systemService.deleteSystem(systemId));
  }
}
