package com.DevXD.demo.DINT;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveResponse {
    private Long id;
    private String employeeName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String status; // PENDING, APPROVED, REJECTED
    private LocalDateTime createdAt;
    private LocalDateTime reviewedAt;
    private String reviewedByName;
}
