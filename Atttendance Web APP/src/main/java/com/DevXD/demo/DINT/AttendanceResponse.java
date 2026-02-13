package com.DevXD.demo.DINT;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceResponse {
    private Long id;
    private String type; // CHECK_IN or CHECK_OUT
    private LocalDateTime timestamp;
    private Double latitude;
    private Double longitude;
    private String photoUrl;
    private Boolean withinGeofence;
    private Double distanceFromOffice;
    private String message;
}
