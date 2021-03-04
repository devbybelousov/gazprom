package com.gazprom.system.controller;

import com.gazprom.system.payload.ApplicationRequest;
import com.gazprom.system.service.RequestService;
import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/api/request")
@AllArgsConstructor
public class RequestController {

  private final RequestService requestService;

  @PostMapping()
  ResponseEntity<?> addRequest(@RequestBody ApplicationRequest request) {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(requestService.addRequest(request));
  }

  @GetMapping()
  ResponseEntity<?> getUserAllRequest(HttpServletRequest request) {
    Long id;
    if (request.getParameter("userId") != null) {
      id = Long.valueOf(request.getParameter("userId"));
    } else {
      return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
          .body(-1);
    }

    int filterNumber;
    if (request.getParameter("filter") != null) {
      filterNumber = Integer.parseInt(request.getParameter("filter"));
    } else {
      return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
          .body(requestService.getAllUserRequest(id));
    }
    switch (filterNumber) {
      case 1:
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
            .body(requestService.getAllRequestByStatus(id, request.getParameter("status")));
      case 2:
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
            .body(requestService.getAllRequestByDate(id, request.getParameter("date")));
      case 3:
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
            .body(requestService.getAllRequestBySystem(id,
                Long.valueOf(request.getParameter("systemId"))));
      default:
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
            .body(requestService.getAllUserRequest(id));
    }
  }

  @GetMapping("/admin")
  ResponseEntity<?> getAllAdminRequest(@RequestParam("userId") Long id) {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(requestService.getAllAdminRequest(id));
  }

  @GetMapping("/owner")
  ResponseEntity<?> getAllOwnerRequest(@RequestParam("userId") Long id) {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(requestService.getAllOwnerRequest(id));
  }

  @GetMapping("/history")
  ResponseEntity<?> getHistoryByRequest(@RequestParam(name = "id") Long id) {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(requestService.getHistoryByRequest(id));
  }

  @GetMapping("/check/owner")
  ResponseEntity<?> checkRequestByOwner(HttpServletRequest request) {
    if (!(request.getParameter("userId") != null && request.getParameter("id") != null))
      return ResponseEntity.ok(-1);

    Long id = Long.valueOf(request.getParameter("id"));
    Long userId = Long.valueOf(request.getParameter("userId"));

    if (request.getAttribute("reason") != null) {
      return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
          .body(requestService.rejectionOfRequestByOwner(
              id,
              userId,
              String.valueOf(request.getAttribute("reason"))));
    }
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(requestService.approvalOfApplicationByOwner(id, userId));
  }

  @GetMapping("/check/admin")
  ResponseEntity<?> checkRequestByAdmin(HttpServletRequest request) {
    if (!(request.getParameter("userId") != null && request.getParameter("id") != null))
      return ResponseEntity.ok(-1);

    Long id = Long.valueOf(request.getParameter("id"));
    Long userId = Long.valueOf(request.getParameter("userId"));

    if (request.getParameter("reason") != null) {
      return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
          .body(requestService.rejectionOfRequestByAdmin(
              id,
              userId,
             request.getParameter("reason")));
    }
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(requestService.approvalOfApplicationByAdmin(id, userId));
  }

  @GetMapping("/info")
  ResponseEntity<?> getRequestInfo(@RequestParam("id") Long id) {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(requestService.getRequestInfo(id));
  }

  @DeleteMapping()
  public ResponseEntity<?> deleteRequest(@RequestParam(name = "id") Long requestId) {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(requestService.deleteRequest(requestId));
  }
}
