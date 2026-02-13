package com.DevXD.demo.controller;

import com.DevXD.demo.DINT.LeaveRequestDto;
import com.DevXD.demo.DINT.LeaveResponse;
import com.DevXD.demo.repository.UserRepository;
import com.DevXD.demo.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave")
@CrossOrigin(origins = "*")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Submit leave request
     */
    @PostMapping("/submit")
    public ResponseEntity<?> submitLeaveRequest(@RequestBody LeaveRequestDto request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Long userId = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"))
                    .getId();

            LeaveResponse response = leaveService.submitLeaveRequest(userId, request);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get user's leave requests
     */
    @GetMapping("/my-requests")
    public ResponseEntity<?> getMyLeaveRequests() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Long userId = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"))
                    .getId();

            List<LeaveResponse> requests = leaveService.getUserLeaveRequests(userId);
            return ResponseEntity.ok(requests);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get all leave requests (Admin only)
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllLeaveRequests() {
        try {
            List<LeaveResponse> requests = leaveService.getAllLeaveRequests();
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get pending leave requests (Admin only)
     */
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getPendingLeaveRequests() {
        try {
            List<LeaveResponse> requests = leaveService.getPendingLeaveRequests();
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Approve leave request (Admin only)
     */
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveLeaveRequest(@PathVariable Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Long adminId = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"))
                    .getId();

            LeaveResponse response = leaveService.approveLeaveRequest(id, adminId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Reject leave request (Admin only)
     */
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> rejectLeaveRequest(@PathVariable Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Long adminId = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"))
                    .getId();

            LeaveResponse response = leaveService.rejectLeaveRequest(id, adminId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}