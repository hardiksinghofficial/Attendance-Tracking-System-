package com.DevXD.demo.service;

import com.DevXD.demo.DINT.UpdateOfficeSettingsRequest;
import com.DevXD.demo.model.OfficeSettings;
import com.DevXD.demo.repository.OfficeSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private OfficeSettingsRepository officeSettingsRepository;

    /**
     * Get office settings
     */
    public OfficeSettings getOfficeSettings() {
        return officeSettingsRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Office settings not configured"));
    }

    /**
     * Update office settings
     */
    public OfficeSettings updateOfficeSettings(UpdateOfficeSettingsRequest request) {
        OfficeSettings settings = officeSettingsRepository.findAll().stream().findFirst()
                .orElse(new OfficeSettings());

        if (request.getOfficeLatitude() != null) {
            settings.setOfficeLatitude(request.getOfficeLatitude());
        }

        if (request.getOfficeLongitude() != null) {
            settings.setOfficeLongitude(request.getOfficeLongitude());
        }

        if (request.getGeofenceRadius() != null) {
            settings.setGeofenceRadius(request.getGeofenceRadius());
        }

        if (request.getCheckInStartTime() != null) {
            settings.setCheckInStartTime(request.getCheckInStartTime());
        }

        if (request.getCheckInEndTime() != null) {
            settings.setCheckInEndTime(request.getCheckInEndTime());
        }

        if (request.getCheckOutStartTime() != null) {
            settings.setCheckOutStartTime(request.getCheckOutStartTime());
        }

        if (request.getCheckOutEndTime() != null) {
            settings.setCheckOutEndTime(request.getCheckOutEndTime());
        }

        return officeSettingsRepository.save(settings);
    }
}