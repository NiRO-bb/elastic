package com.example.elastic_api_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories
public class ElasticApiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElasticApiServiceApplication.class, args);
	}

}
