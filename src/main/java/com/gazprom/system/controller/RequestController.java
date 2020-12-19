package com.gazprom.system.controller;

import com.gazprom.system.payload.ApiResponse;
import com.gazprom.system.payload.ApplicationRequest;
import com.gazprom.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/request")
public class RequestController {

    @Autowired
    UserService userService;

    @GetMapping("/all")
    ResponseEntity<?> getUserAllRequest(@RequestParam(name = "userId") Long id){
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(userService.getAllUserRequest(id));
    }

    @GetMapping("/all/admin")
    ResponseEntity<?> getAllAdminRequest(@RequestParam("userId") Long id){
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(userService.getAllAdminRequest(id));
    }

    @GetMapping("/all/owner")
    ResponseEntity<?> getAllOwnerRequest(@RequestParam("userId") Long id){
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(userService.getAllOwnerRequest(id));
    }

    @GetMapping("/all/active")
    ResponseEntity<?> getAllActiveRequest(@RequestParam(name = "userId") Long id){
        return ResponseEntity.ok(userService.getAllRequestByStatusEnable(id));
    }

    @PostMapping("/add")
    ResponseEntity<?> addRequest(@RequestBody ApplicationRequest request){
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new ApiResponse(userService.addRequest(request), "Request add successfully"));
    }

    @GetMapping("/history")
    ResponseEntity<?> getHistoryByRequest(@RequestParam(name = "requestId") Long id){
        return ResponseEntity.ok(userService.getHistoryByRequest(id));
    }

    @GetMapping("/approval/owner")
    ResponseEntity<?> approvalOfApplicationByOwner(@RequestParam(name = "requestId") Long id, @RequestParam Long userId){
        if (!userService.approvalOfApplicationByOwner(id, userId))
            return new ResponseEntity(new ApiResponse(false, "Invalid user!"),
                    HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(new ApiResponse(true,
                "The application was approved successfully"));
    }

    @GetMapping("/approval/admin")
    ResponseEntity<?> approvalOfApplicationByAdmin(@RequestParam(name = "requestId") Long id, @RequestParam Long userId){
        if (!userService.approvalOfApplicationByAdmin(id, userId))
            return new ResponseEntity(new ApiResponse(false, "Invalid user!"),
                    HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(new ApiResponse(true,
                "The application was approved successfully"));
    }

    @GetMapping("/rejection/owner")
    ResponseEntity<?> rejectionOfRequestByOwner(@RequestParam(name = "requestId") Long id, @RequestParam Long userId, @RequestParam String reason){
        if (!userService.rejectionOfRequestByOwner(id, userId,reason))
            return new ResponseEntity(new ApiResponse(false, "Invalid user!"),
                    HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(new ApiResponse(true,
                "The application was rejection successfully"));
    }

    @GetMapping("/rejection/admin")
    ResponseEntity<?> rejectionOfRequestByAdmin(@RequestParam(name = "requestId") Long id, @RequestParam Long userId, @RequestParam String reason){
        if (!userService.rejectionOfRequestByAdmin(id, userId, reason))
            return new ResponseEntity(new ApiResponse(false, "Invalid user!"),
                    HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(new ApiResponse(true,
                "The application was rejection successfully"));
    }
}
