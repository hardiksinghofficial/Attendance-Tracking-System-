package com.DevXD.demo.repository;


import com.DevXD.demo.model.OfficeSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfficeSettingsRepository extends JpaRepository<OfficeSettings, Long> {
}