package com.DevXD.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;
import java.util.TimeZone;
import java.util.TimeZone;

@SpringBootApplication
public class AttendanceManagementSystemApplication {


	public static void main(String[] args) {

		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		SpringApplication.run(AttendanceManagementSystemApplication.class, args);
	}

}
