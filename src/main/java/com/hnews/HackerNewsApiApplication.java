package com.hnews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableCaching
public class HackerNewsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(HackerNewsApiApplication.class, args);
	}
	
	 @Bean
		public RestTemplate restTemplate() {
			return new RestTemplate();
		}

}
