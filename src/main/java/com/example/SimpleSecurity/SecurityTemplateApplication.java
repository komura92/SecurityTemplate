package com.example.SimpleSecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// TODO:
//log4j instead of Slf4j
//.properties file instead of actual static configuration
@SpringBootApplication
public class SecurityTemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityTemplateApplication.class, args);
	}

}
