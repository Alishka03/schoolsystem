package com.example.schoolsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootApplication
public class SchoolsystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolsystemApplication.class, args);
	}
}
