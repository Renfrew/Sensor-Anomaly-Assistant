package com.thinkquark.saa;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class ApiJavaApplication {

	@Autowired
	private Environment env;

	@PostConstruct
	void logProfile() {
		System.out.println("Active profiles: " + Arrays.toString(env.getActiveProfiles()));
	}

	public static void main(String[] args) {
		SpringApplication.run(ApiJavaApplication.class, args);
	}

}
