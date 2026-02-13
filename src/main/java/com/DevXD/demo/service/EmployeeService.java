package com.DevXD.demo.service;

import com.DevXD.demo.DINT.CreateEmployeeRequest;
import com.DevXD.demo.DINT.EmployeeResponse;
import com.DevXD.demo.DINT.UpdateEmployeeRequest;
import com.DevXD.demo.model.User;
import com.DevXD.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ImageUploadService imageUploadService;

    /**
     * Create new employee
     */
    public EmployeeResponse createEmployee(CreateEmployeeRequest request) throws Exception {
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Upload profile photo if provided
        String profilePhotoUrl = null;
        if (request.getProfilePhotoBase64() != null && !request.getProfilePhotoBase64().isEmpty()) {
            profilePhotoUrl = imageUploadService.uploadImage(request.getProfilePhotoBase64(), "profiles");
        }

        // Create user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setRole("EMPLOYEE");
        user.setProfilePhotoUrl(profilePhotoUrl);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());

        user = userRepository.save(user);

        return mapToEmployeeResponse(user);
    }

    /**
     * Get all employees
     */
    public List<EmployeeResponse> getAllEmployees() {
        return userRepository.findAll().stream()
                .map(this::mapToEmployeeResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get employee by ID
     */
    public EmployeeResponse getEmployeeById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return mapToEmployeeResponse(user);
    }

    /**
     * Update employee
     */
    public EmployeeResponse updateEmployee(Long id, UpdateEmployeeRequest request) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Update fields if provided
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }

        if (request.getEmail() != null) {
            // Check if email is already taken by another user
            if (userRepository.existsByEmail(request.getEmail()) &&
                    !user.getEmail().equals(request.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getActive() != null) {
            user.setActive(request.getActive());
        }

        // Update profile photo if provided
        if (request.getProfilePhotoBase64() != null && !request.getProfilePhotoBase64().isEmpty()) {
            // Delete old photo if exists
            if (user.getProfilePhotoUrl() != null) {
                imageUploadService.deleteImage(user.getProfilePhotoUrl());
            }
            // Upload new photo
            String newPhotoUrl = imageUploadService.uploadImage(request.getProfilePhotoBase64(), "profiles");
            user.setProfilePhotoUrl(newPhotoUrl);
        }

        user = userRepository.save(user);

        return mapToEmployeeResponse(user);
    }

    /**
     * Delete employee
     */
    public void deleteEmployee(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Delete profile photo if exists
        if (user.getProfilePhotoUrl() != null) {
            imageUploadService.deleteImage(user.getProfilePhotoUrl());
        }

        userRepository.delete(user);
    }

    /**
     * Map User entity to EmployeeResponse DTO
     */
    private EmployeeResponse mapToEmployeeResponse(User user) {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setProfilePhotoUrl(user.getProfilePhotoUrl());
        response.setActive(user.getActive());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}