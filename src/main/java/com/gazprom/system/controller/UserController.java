package com.gazprom.system.controller;

import com.gazprom.system.payload.ApiResponse;
import com.gazprom.system.payload.ApplicationRequest;
import com.gazprom.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/all/systems")
    public ResponseEntity<?> getAllSystem(){
        return ResponseEntity.ok(userService.getAllSystem());
    }

    @GetMapping("/all/user")
    public ResponseEntity<?> getAllUser(){
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/user/info")
    ResponseEntity<?> getUserInfo(@RequestParam Long id){
        return ResponseEntity.ok(userService.getUserInfo(id));
    }

    @GetMapping("/all/user/request")
    ResponseEntity<?> getUserAllRequest(@RequestParam Long id){
        return ResponseEntity.ok(userService.getAllUserRequest(id));
    }

    @GetMapping("/all/active/request")
    ResponseEntity<?> getAllActiveRequest(@RequestParam Long id){
        return ResponseEntity.ok(userService.getAllActiveRequest(id));
    }

    @PostMapping("/add/request")
    ResponseEntity<?> addRequest(@RequestBody ApplicationRequest request){
        if(!userService.addRequest(request))
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(new ApiResponse(true, "Request add successfully"));
    }
}
