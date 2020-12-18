package com.gazprom.system.controller;

import com.gazprom.system.payload.ApiResponse;
import com.gazprom.system.payload.SystemRequest;
import com.gazprom.system.payload.UserRequest;
import com.gazprom.system.repository.UserRepository;
import com.gazprom.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create/user")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest signUpRequest) {
        if(!userService.createUser(signUpRequest))
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(new ApiResponse(true, "User registered successfully"));
    }

    @GetMapping("/exists/user")
    public ResponseEntity<?> isUserNameExists(@RequestParam("userName") String userName){
        if (!userRepository.existsByUserName(userName))
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(new ApiResponse(true, "Username if free."));
    }

    @PostMapping("/system/add")
    public ResponseEntity<?> createSystem(@RequestBody SystemRequest systemRequest){
        return ResponseEntity.ok(new ApiResponse(userService.addSystem(systemRequest), "System created successfully."));
    }

    @GetMapping("/department/add")
    public ResponseEntity<?> createDepartment(@RequestParam String title, @RequestParam Long unitId){
        return ResponseEntity.ok(new ApiResponse(userService.addDepartment(title, unitId), "Department created successfully."));
    }

    @GetMapping("/privilege/add")
    public ResponseEntity<?> createPrivilege(@RequestParam String title, @RequestParam String description){
        return ResponseEntity.ok(new ApiResponse(userService.addPrivilege(title, description), "Privilege created successfully."));
    }

    @GetMapping("/unit/add")
    public ResponseEntity<?> createUnit(@RequestParam String title){
        return ResponseEntity.ok(new ApiResponse(userService.addUnit(title), "Unit created successfully."));
    }

    @GetMapping("/system/delete")
    public ResponseEntity<?> deleteSystem(@RequestParam Long systemId){
        return ResponseEntity.ok(new ApiResponse(userService.deleteSystem(systemId), "System deleted successfully."));
    }

    @GetMapping("/department/add")
    public ResponseEntity<?> deleteDepartment(@RequestParam Long departmentId){
        return ResponseEntity.ok(new ApiResponse(userService.deleteDepartment(departmentId), "Department deleted successfully."));
    }

    @GetMapping("/privilege/add")
    public ResponseEntity<?> deletePrivilege(@RequestParam Long privilegeId){
        return ResponseEntity.ok(new ApiResponse(userService.deletePrivilege(privilegeId), "Privilege deleted successfully."));
    }

    @GetMapping("/unit/add")
    public ResponseEntity<?> deleteUnit(@RequestParam Long unitId){
        return ResponseEntity.ok(new ApiResponse(userService.deleteUnit(unitId), "Unit deleted successfully."));
    }

    @GetMapping("/user/add")
    public ResponseEntity<?> deleteUser(@RequestParam Long userId){
        return ResponseEntity.ok(new ApiResponse(userService.deleteUser(userId), "User deleted successfully."));
    }

    @GetMapping("/request/add")
    public ResponseEntity<?> deleteRequest(@RequestParam Long requestId){
        return ResponseEntity.ok(new ApiResponse(userService.deleteRequest(requestId), "Request deleted successfully."));
    }


}
