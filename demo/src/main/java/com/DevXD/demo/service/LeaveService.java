package com.DevXD.demo.service;

import com.DevXD.demo.DINT.LeaveRequestDto;
import com.DevXD.demo.DINT.LeaveResponse;
import com.DevXD.demo.model.LeaveRequest;
import com.DevXD.demo.model.User;
import com.DevXD.demo.repository.LeaveRequestRepository;
import com.DevXD.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Submit leave request
     */
    public LeaveResponse submitLeaveRequest(Long userId, LeaveRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate dates
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new RuntimeException("End date must be after start date");
        }

        // Create leave request
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setUser(user);
        leaveRequest.setStartDate(request.getStartDate());
        leaveRequest.setEndDate(request.getEndDate());
        leaveRequest.setReason(request.getReason());
        leaveRequest.setStatus("PENDING");
        leaveRequest.setCreatedAt(LocalDateTime.now());

        leaveRequest = leaveRequestRepository.save(leaveRequest);

        return mapToLeaveResponse(leaveRequest);
    }

    /**
     * Get user's leave requests
     */
    public List<LeaveResponse> getUserLeaveRequests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return leaveRequestRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(this::mapToLeaveResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all leave requests (Admin only)
     */
    public List<LeaveResponse> getAllLeaveRequests() {
        return leaveRequestRepository.findAll().stream()
                .map(this::mapToLeaveResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get pending leave requests (Admin only)
     */
    public List<LeaveResponse> getPendingLeaveRequests() {
        return leaveRequestRepository.findByStatus("PENDING").stream()
                .map(this::mapToLeaveResponse)
                .collect(Collectors.toList());
    }

    /**
     * Approve leave request (Admin only)
     */
    public LeaveResponse approveLeaveRequest(Long leaveId, Long adminId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        if (!"PENDING".equals(leaveRequest.getStatus())) {
            throw new RuntimeException("Leave request already processed");
        }

        leaveRequest.setStatus("APPROVED");
        leaveRequest.setReviewedAt(LocalDateTime.now());
        leaveRequest.setReviewedBy(adminId);

        leaveRequest = leaveRequestRepository.save(leaveRequest);

        return mapToLeaveResponse(leaveRequest);
    }

    /**
     * Reject leave request (Admin only)
     */
    public LeaveResponse rejectLeaveRequest(Long leaveId, Long adminId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        if (!"PENDING".equals(leaveRequest.getStatus())) {
            throw new RuntimeException("Leave request already processed");
        }

        leaveRequest.setStatus("REJECTED");
        leaveRequest.setReviewedAt(LocalDateTime.now());
        leaveRequest.setReviewedBy(adminId);

        leaveRequest = leaveRequestRepository.save(leaveRequest);

        return mapToLeaveResponse(leaveRequest);
    }

    /**
     * Map LeaveRequest entity to LeaveResponse DTO
     */
    private LeaveResponse mapToLeaveResponse(LeaveRequest leaveRequest) {
        LeaveResponse response = new LeaveResponse();
        response.setId(leaveRequest.getId());
        response.setEmployeeName(leaveRequest.getUser().getFullName());
        response.setStartDate(leaveRequest.getStartDate());
        response.setEndDate(leaveRequest.getEndDate());
        response.setReason(leaveRequest.getReason());
        response.setStatus(leaveRequest.getStatus());
        response.setCreatedAt(leaveRequest.getCreatedAt());
        response.setReviewedAt(leaveRequest.getReviewedAt());

        // Get reviewer name if reviewed
        if (leaveRequest.getReviewedBy() != null) {
            userRepository.findById(leaveRequest.getReviewedBy())
                    .ifPresent(reviewer -> response.setReviewedByName(reviewer.getFullName()));
        }

        return response;
    }
}