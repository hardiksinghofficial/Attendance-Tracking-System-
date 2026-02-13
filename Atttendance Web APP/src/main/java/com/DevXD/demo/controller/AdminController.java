package com.DevXD.demo.controller;




import com.DevXD.demo.DINT.UpdateOfficeSettingsRequest;
import com.DevXD.demo.model.OfficeSettings;
import com.DevXD.demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * Get office settings (Admin only)
     */
    @GetMapping("/settings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getOfficeSettings() {
        try {
            OfficeSettings settings = adminService.getOfficeSettings();
            return ResponseEntity.ok(settings);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Update office settings (Admin only)
     */
    @PutMapping("/settings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateOfficeSettings(@RequestBody UpdateOfficeSettingsRequest request) {
        try {
            OfficeSettings settings = adminService.updateOfficeSettings(request);
            return ResponseEntity.ok(settings);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}