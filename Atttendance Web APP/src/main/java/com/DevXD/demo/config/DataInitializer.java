package com.DevXD.demo.config;


import com.DevXD.demo.model.OfficeSettings;
import com.DevXD.demo.model.User;
import com.DevXD.demo.repository.OfficeSettingsRepository;
import com.DevXD.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OfficeSettingsRepository officeSettingsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create default admin if not exists
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("System Administrator");
            admin.setEmail("admin@attendance.com");
            admin.setRole("ADMIN");
            admin.setActive(true);
            userRepository.save(admin);
            System.out.println("Default admin created: username=admin, password=admin123");
        }

        // Create default office settings if not exists
        if (officeSettingsRepository.count() == 0) {
            OfficeSettings settings = new OfficeSettings();
            settings.setOfficeLatitude(22.7196);  // Example: Indore
            settings.setOfficeLongitude(75.8577);
            settings.setGeofenceRadius(500.0);
            settings.setCheckInStartTime(LocalTime.of(10, 0));
            settings.setCheckInEndTime(LocalTime.of(11, 30));
            settings.setCheckOutStartTime(LocalTime.of(15, 0));
            settings.setCheckOutEndTime(LocalTime.of(17, 0));
            officeSettingsRepository.save(settings);
            System.out.println("Default office settings created");
        }
    }
}