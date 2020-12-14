package com.gazprom.system.controller;

import com.gazprom.system.payload.ApiResponse;
import com.gazprom.system.payload.ApplicationRequest;
import com.gazprom.system.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllUser(){
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestParam(name = "userId") Long id) {
        return ResponseEntity.ok(userService.getUserInfo(id));
    }

    @GetMapping("/system/info")
    public ResponseEntity<?> getSystemInfo(@RequestParam(name = "systemId") Long id){
        return ResponseEntity.ok(userService.getSystemById(id));
    }

    @GetMapping("/system/all")
    public ResponseEntity<?> getAllSystem(){
        return ResponseEntity.ok(userService.getAllSystem());
    }

    @GetMapping("/unit/all")
    public ResponseEntity<?> getAllUnit(){
        return ResponseEntity.ok(userService.getAllUnit());
    }

    @GetMapping("/department/all")
    public ResponseEntity<?> getAllDepartmentByUnit(@RequestParam (name = "unitId") Long id){
        return ResponseEntity.ok(userService.getAllDepartmentByUnit(id));
    }
}
