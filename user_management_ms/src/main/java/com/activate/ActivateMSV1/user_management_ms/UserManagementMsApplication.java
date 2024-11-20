package com.activate.ActivateMSV1.user_management_ms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserManagementMsApplication {


	public static void main(String[] args) {

		SpringApplication.run(UserManagementMsApplication.class, args);
		Logger logger = LoggerFactory.getLogger(UserManagementMsApplication.class);
		logger.info("Corriendo con actuator");
	}

}
