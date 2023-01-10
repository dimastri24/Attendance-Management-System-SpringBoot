package com.dimas.jumpstart.attendance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.dimas.jumpstart.attendance.config.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class JumpstartAttendanceManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(JumpstartAttendanceManagementSystemApplication.class, args);
	}

}
