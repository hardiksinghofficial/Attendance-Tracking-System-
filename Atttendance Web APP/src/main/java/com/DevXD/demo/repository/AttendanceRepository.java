package com.DevXD.demo.repository;

import com.DevXD.demo.model.Attendance;
import com.DevXD.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByUserOrderByTimestampDesc(User user);
    List<Attendance> findByUserAndTimestampBetween(User user, LocalDateTime start, LocalDateTime end);
    Optional<Attendance> findFirstByUserAndTypeOrderByTimestampDesc(User user, String type);
}