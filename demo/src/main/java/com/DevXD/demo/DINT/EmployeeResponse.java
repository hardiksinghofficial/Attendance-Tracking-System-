package com.DevXD.demo.DINT;


import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String role;
    private String profilePhotoUrl;
    private Boolean active;
    private LocalDateTime createdAt;
}