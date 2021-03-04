package com.gazprom.system.controller;

import com.gazprom.system.payload.DepartmentRequest;
import com.gazprom.system.service.DepartmentService;
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
@RequestMapping("/api/department")
@AllArgsConstructor
public class DepartmentController {

  private final DepartmentService departmentService;

  @PostMapping()
  public ResponseEntity<?> createDepartment(@RequestBody DepartmentRequest departmentRequest) {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(departmentService.addDepartment(departmentRequest));
  }

  @GetMapping()
  public ResponseEntity<?> getAllDepartmentByUnit(@RequestParam(name = "unitId") Long id) {
    return ResponseEntity.ok(departmentService.getAllDepartmentByUnit(id));
  }

  @DeleteMapping()
  public ResponseEntity<?> deleteDepartment(@RequestParam Long departmentId) {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(departmentService.deleteDepartment(departmentId));
  }
}
