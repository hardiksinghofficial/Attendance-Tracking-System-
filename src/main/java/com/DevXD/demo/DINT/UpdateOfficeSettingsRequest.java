package com.DevXD.demo.DINT;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOfficeSettingsRequest {
    private Double officeLatitude;
    private Double officeLongitude;
    private Double geofenceRadius;
    private LocalTime checkInStartTime;
    private LocalTime checkInEndTime;
    private LocalTime checkOutStartTime;
    private LocalTime checkOutEndTime;
}