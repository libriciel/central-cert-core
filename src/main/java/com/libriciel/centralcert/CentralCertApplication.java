package com.libriciel.centralcert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ServletComponentScan
@EnableScheduling
@SpringBootApplication
public class CentralCertApplication {
	public static void main(String[] args) {
		SpringApplication.run(CentralCertApplication.class, args);
	}
}
