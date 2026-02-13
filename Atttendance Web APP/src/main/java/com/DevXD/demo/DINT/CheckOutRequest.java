package com.DevXD.demo.DINT;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckOutRequest {
    private Double latitude;
    private Double longitude;
    private String photoBase64;
    private String deviceId;
}