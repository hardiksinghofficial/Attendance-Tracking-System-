package com.DevXD.demo.service;

import com.DevXD.demo.DINT.AttendanceHistoryResponse;
import com.DevXD.demo.DINT.AttendanceResponse;
import com.DevXD.demo.DINT.CheckInRequest;
import com.DevXD.demo.DINT.CheckOutRequest;
import com.DevXD.demo.model.Attendance;
import com.DevXD.demo.model.OfficeSettings;
import com.DevXD.demo.model.User;
import com.DevXD.demo.repository.AttendanceRepository;
import com.DevXD.demo.repository.OfficeSettingsRepository;
import com.DevXD.demo.repository.UserRepository;
import com.DevXD.demo.util.GeolocationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OfficeSettingsRepository officeSettingsRepository;

    @Autowired
    private ImageUploadService imageUploadService;

    /**
     * Process check-in
     */
    public AttendanceResponse checkIn(Long userId, CheckInRequest request) throws Exception {
        // Get user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get office settings
        OfficeSettings settings = officeSettingsRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Office settings not configured"));

        // Validate time window
        LocalTime now = LocalTime.now();
        if (now.isBefore(settings.getCheckInStartTime()) || now.isAfter(settings.getCheckInEndTime())) {
            throw new RuntimeException(
                    String.format("Check-in only allowed between %s and %s",
                            settings.getCheckInStartTime(),
                            settings.getCheckInEndTime())
            );
        }

        // Check if already checked in today
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);
        List<Attendance> todayAttendance = attendanceRepository.findByUserAndTimestampBetween(
                user, startOfDay, endOfDay
        );

        boolean alreadyCheckedIn = todayAttendance.stream()
                .anyMatch(a -> "CHECK_IN".equals(a.getType()));

        if (alreadyCheckedIn) {
            throw new RuntimeException("You have already checked in today");
        }

        // Calculate distance from office
        double distance = GeolocationUtil.calculateDistance(
                request.getLatitude(),
                request.getLongitude(),
                settings.getOfficeLatitude(),
                settings.getOfficeLongitude()
        );

        // Check geofence
        boolean withinGeofence = distance <= settings.getGeofenceRadius();

        if (!withinGeofence) {
            throw new RuntimeException(
                    String.format("You are %.2f meters away from office. Must be within %.0f meters.",
                            distance, settings.getGeofenceRadius())
            );
        }

        // Upload photo to Cloudinary
        String photoUrl = null;
        if (request.getPhotoBase64() != null && !request.getPhotoBase64().isEmpty()) {
            photoUrl = imageUploadService.uploadImage(request.getPhotoBase64(), "attendance");
        }

        // Create attendance record
        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setTimestamp(LocalDateTime.now());
        attendance.setType("CHECK_IN");
        attendance.setLatitude(request.getLatitude());
        attendance.setLongitude(request.getLongitude());
        attendance.setPhotoUrl(photoUrl);
        attendance.setWithinGeofence(withinGeofence);
        attendance.setDistanceFromOffice(distance);

        // Save to database
        attendance = attendanceRepository.save(attendance);

        // Create response
        AttendanceResponse response = new AttendanceResponse();
        response.setId(attendance.getId());
        response.setType(attendance.getType());
        response.setTimestamp(attendance.getTimestamp());
        response.setLatitude(attendance.getLatitude());
        response.setLongitude(attendance.getLongitude());
        response.setPhotoUrl(attendance.getPhotoUrl());
        response.setWithinGeofence(attendance.getWithinGeofence());
        response.setDistanceFromOffice(attendance.getDistanceFromOffice());
        response.setMessage("Checked in successfully at " + LocalTime.now().toString());

        return response;
    }

    /**
     * Process check-out
     */
    public AttendanceResponse checkOut(Long userId, CheckOutRequest request) throws Exception {
        // Get user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get office settings
        OfficeSettings settings = officeSettingsRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Office settings not configured"));

        // Validate time window
        LocalTime now = LocalTime.now();
        if (now.isBefore(settings.getCheckOutStartTime()) || now.isAfter(settings.getCheckOutEndTime())) {
            throw new RuntimeException(
                    String.format("Check-out only allowed between %s and %s",
                            settings.getCheckOutStartTime(),
                            settings.getCheckOutEndTime())
            );
        }

        // Check if checked in today
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);
        List<Attendance> todayAttendance = attendanceRepository.findByUserAndTimestampBetween(
                user, startOfDay, endOfDay
        );

        boolean hasCheckedIn = todayAttendance.stream()
                .anyMatch(a -> "CHECK_IN".equals(a.getType()));

        if (!hasCheckedIn) {
            throw new RuntimeException("You must check in before checking out");
        }

        boolean alreadyCheckedOut = todayAttendance.stream()
                .anyMatch(a -> "CHECK_OUT".equals(a.getType()));

        if (alreadyCheckedOut) {
            throw new RuntimeException("You have already checked out today");
        }

        // Calculate distance from office
        double distance = GeolocationUtil.calculateDistance(
                request.getLatitude(),
                request.getLongitude(),
                settings.getOfficeLatitude(),
                settings.getOfficeLongitude()
        );

        // Check geofence
        boolean withinGeofence = distance <= settings.getGeofenceRadius();

        if (!withinGeofence) {
            throw new RuntimeException(
                    String.format("You are %.2f meters away from office. Must be within %.0f meters.",
                            distance, settings.getGeofenceRadius())
            );
        }

        // Upload photo to Cloudinary
        String photoUrl = null;
        if (request.getPhotoBase64() != null && !request.getPhotoBase64().isEmpty()) {
            photoUrl = imageUploadService.uploadImage(request.getPhotoBase64(), "attendance");
        }

        // Create attendance record
        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setTimestamp(LocalDateTime.now());
        attendance.setType("CHECK_OUT");
        attendance.setLatitude(request.getLatitude());
        attendance.setLongitude(request.getLongitude());
        attendance.setPhotoUrl(photoUrl);
        attendance.setWithinGeofence(withinGeofence);
        attendance.setDistanceFromOffice(distance);

        // Save to database
        attendance = attendanceRepository.save(attendance);

        // Create response
        AttendanceResponse response = new AttendanceResponse();
        response.setId(attendance.getId());
        response.setType(attendance.getType());
        response.setTimestamp(attendance.getTimestamp());
        response.setLatitude(attendance.getLatitude());
        response.setLongitude(attendance.getLongitude());
        response.setPhotoUrl(attendance.getPhotoUrl());
        response.setWithinGeofence(attendance.getWithinGeofence());
        response.setDistanceFromOffice(attendance.getDistanceFromOffice());
        response.setMessage("Checked out successfully at " + LocalTime.now().toString());

        return response;
    }

    /**
     * Get user's attendance history
     */
    public List<AttendanceHistoryResponse> getUserAttendanceHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Attendance> attendanceList = attendanceRepository.findByUserOrderByTimestampDesc(user);

        return attendanceList.stream().map(a -> {
            AttendanceHistoryResponse response = new AttendanceHistoryResponse();
            response.setId(a.getId());
            response.setEmployeeName(user.getFullName());
            response.setType(a.getType());
            response.setTimestamp(a.getTimestamp());
            response.setLatitude(a.getLatitude());
            response.setLongitude(a.getLongitude());
            response.setPhotoUrl(a.getPhotoUrl());
            response.setWithinGeofence(a.getWithinGeofence());
            response.setDistanceFromOffice(a.getDistanceFromOffice());
            return response;
        }).collect(Collectors.toList());
    }

    /**
     * Get all attendance records (Admin only)
     */
    public List<AttendanceHistoryResponse> getAllAttendance() {
        List<Attendance> attendanceList = attendanceRepository.findAll();

        return attendanceList.stream().map(a -> {
            AttendanceHistoryResponse response = new AttendanceHistoryResponse();
            response.setId(a.getId());
            response.setEmployeeName(a.getUser().getFullName());
            response.setType(a.getType());
            response.setTimestamp(a.getTimestamp());
            response.setLatitude(a.getLatitude());
            response.setLongitude(a.getLongitude());
            response.setPhotoUrl(a.getPhotoUrl());
            response.setWithinGeofence(a.getWithinGeofence());
            response.setDistanceFromOffice(a.getDistanceFromOffice());
            return response;
        }).collect(Collectors.toList());
    }
}