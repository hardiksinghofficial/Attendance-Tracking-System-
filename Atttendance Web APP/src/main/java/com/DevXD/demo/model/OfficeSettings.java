package com.DevXD.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Entity
@Table(name = "office_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfficeSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double officeLatitude;

    @Column(nullable = false)
    private Double officeLongitude;

    @Column(nullable = false)
    private Double geofenceRadius = 500.0; // meters

    @Column(nullable = false)
    private LocalTime checkInStartTime; // 10:00

    @Column(nullable = false)
    private LocalTime checkInEndTime; // 11:30

    @Column(nullable = false)
    private LocalTime checkOutStartTime; // 15:00

    @Column(nullable = false)
    private LocalTime checkOutEndTime; // 17:00
}