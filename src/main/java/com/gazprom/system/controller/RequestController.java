package com.gazprom.system.controller;

import com.gazprom.system.payload.ApiResponse;
import com.gazprom.system.payload.ApplicationRequest;
import com.gazprom.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/status/active")
    ResponseEntity<?> setActiveStatusRequest(@RequestParam(name = "requestId") Long id){
        return ResponseEntity.ok(new ApiResponse(userService.setActiveRequest(id),
                "The status was changed successfully"));
    }

    @GetMapping("/approval/owner")
    ResponseEntity<?> approvalOfApplicationByOwner(@RequestParam(name = "requestId") Long id){
        return ResponseEntity.ok(new ApiResponse(userService.approvalOfApplicationByOwner(id),
                "The application was approved successfully"));
    }

    @GetMapping("/approval/admin")
    ResponseEntity<?> approvalOfApplicationByAdmin(@RequestParam(name = "requestId") Long id){
        return ResponseEntity.ok(new ApiResponse(userService.approvalOfApplicationByAdmin(id),
                "The application was approved successfully"));
    }

    @GetMapping("/rejection/owner")
    ResponseEntity<?> rejectionOfRequestByOwner(@RequestParam(name = "requestId") Long id){
        return ResponseEntity.ok(new ApiResponse(userService.approvalOfApplicationByAdmin(id),
                "The application was approved successfully"));
    }
}
