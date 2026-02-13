package com.DevXD.demo.controller;


import com.DevXD.demo.DINT.AttendanceHistoryResponse;
import com.DevXD.demo.DINT.AttendanceResponse;
import com.DevXD.demo.DINT.CheckInRequest;
import com.DevXD.demo.DINT.CheckOutRequest;
import com.DevXD.demo.repository.UserRepository;
import com.DevXD.demo.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Check-in endpoint
     */
    @PostMapping("/check-in")
    public ResponseEntity<?> checkIn(@RequestBody CheckInRequest request) {
        try {
            // Get current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Long userId = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"))
                    .getId();

            AttendanceResponse response = attendanceService.checkIn(userId, request);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Check-out endpoint
     */
    @PostMapping("/check-out")
    public ResponseEntity<?> checkOut(@RequestBody CheckOutRequest request) {
        try {
            // Get current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Long userId = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"))
                    .getId();

            AttendanceResponse response = attendanceService.checkOut(userId, request);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get user's own attendance history
     */
    @GetMapping("/my-history")
    public ResponseEntity<?> getMyHistory() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Long userId = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"))
                    .getId();

            List<AttendanceHistoryResponse> history = attendanceService.getUserAttendanceHistory(userId);
            return ResponseEntity.ok(history);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get all attendance records (Admin only)
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllAttendance() {
        try {
            List<AttendanceHistoryResponse> history = attendanceService.getAllAttendance();
            return ResponseEntity.ok(history);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
