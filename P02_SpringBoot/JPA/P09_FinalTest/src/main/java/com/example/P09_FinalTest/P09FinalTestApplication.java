package com.example.P09_FinalTest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class P09FinalTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(P09FinalTestApplication.class, args);
	}

}
