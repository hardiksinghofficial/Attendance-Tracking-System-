package com.DevXD.demo.DINT;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeRequest {
    private String fullName;
    private String email;
    private String profilePhotoBase64;
    private Boolean active;
}