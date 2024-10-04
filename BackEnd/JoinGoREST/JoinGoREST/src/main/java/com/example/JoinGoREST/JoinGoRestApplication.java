package com.example.JoinGoREST;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication 
@EnableAsync
public class JoinGoRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(JoinGoRestApplication.class, args);
	}


}
