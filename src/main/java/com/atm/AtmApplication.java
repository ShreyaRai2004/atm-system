package com.atm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AtmApplication {
	public static void main(String[] args) {
		SpringApplication.run(AtmApplication.class, args);
		System.out.println("ATM System Started Successfully!");
	}
}