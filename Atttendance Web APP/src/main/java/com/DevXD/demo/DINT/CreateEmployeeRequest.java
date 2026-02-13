package com.DevXD.demo.DINT;



import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class CreateEmployeeRequest {

        @NotBlank(message = "Username is required")
        private String username;

        @NotBlank(message = "Password is required")
        private String password;

        @NotBlank(message = "Full name is required")
        private String fullName;

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required")
        private String email;

        private String profilePhotoBase64;
    }
