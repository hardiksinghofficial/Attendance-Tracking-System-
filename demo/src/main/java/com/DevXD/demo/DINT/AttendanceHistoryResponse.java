package com.DevXD.demo.DINT;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceHistoryResponse {
    private Long id;
    private String employeeName;
    private String type;
    private LocalDateTime timestamp;
    private Double latitude;
    private Double longitude;
    private String photoUrl;
    private Boolean withinGeofence;
    private Double distanceFromOffice;
}
