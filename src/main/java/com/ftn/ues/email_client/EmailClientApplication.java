package com.ftn.ues.email_client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableElasticsearchRepositories(basePackages = "com.ftn.ues.email_client.repository.elastic_search")
@EnableJpaRepositories(basePackages = "com.ftn.ues.email_client.repository.database")
@SpringBootApplication
public class EmailClientApplication {
	public static void main(String[] args) {
		SpringApplication.run(EmailClientApplication.class, args);
	}

}
