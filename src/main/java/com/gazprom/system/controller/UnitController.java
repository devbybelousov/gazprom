package com.gazprom.system.controller;

import com.gazprom.system.payload.UnitRequest;
import com.gazprom.system.service.UnitService;
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
@RequestMapping("/api/unit")
@AllArgsConstructor
public class UnitController {

  private final UnitService unitService;

  @GetMapping()
  public ResponseEntity<?> getAllUnit() {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(unitService.getAllUnit());
  }

  @PostMapping()
  public ResponseEntity<?> createUnit(@RequestBody UnitRequest unitRequest) {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(unitService.addUnit(unitRequest));
  }

  @DeleteMapping()
  public ResponseEntity<?> deleteUnit(@RequestParam(name = "id") Long unitId) {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(unitService.deleteUnit(unitId));
  }
}
