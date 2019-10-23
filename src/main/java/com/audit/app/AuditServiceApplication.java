package com.audit.app;

import org.jasypt.digest.PooledStringDigester;
import org.jasypt.digest.StringDigester;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AuditServiceApplication {

	@Bean
	StringDigester PasswordHash() {
		final PooledStringDigester stringDigester = new PooledStringDigester();
		stringDigester.setAlgorithm("SHA-256");
		stringDigester.setIterations(100000);
		stringDigester.setSaltSizeBytes(10);
		stringDigester.setPoolSize(4);
		stringDigester.initialize();
		return stringDigester; 
	}
	
	public static void main(String[] args) {
		SpringApplication.run(AuditServiceApplication.class, args);
	}

}
