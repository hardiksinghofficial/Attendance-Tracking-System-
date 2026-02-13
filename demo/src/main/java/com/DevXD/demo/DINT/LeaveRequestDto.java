package com.DevXD.demo.DINT;


import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveRequestDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
}