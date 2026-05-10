package com.rfindapi.api_rfind;

import jakarta.persistence.Entity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EntityScan("model")
public class ApiRfindApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiRfindApplication.class, args);
	}

}
