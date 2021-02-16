package com.ftn.ues.email_client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.ftn.ues.email_client.repository.database")
@SpringBootApplication
@ComponentScan(basePackages = "com.ftn.ues.email_client")
public class EmailClientApplication {
	public static void main(String[] args) {
		SpringApplication.run(EmailClientApplication.class, args);
	}

}

