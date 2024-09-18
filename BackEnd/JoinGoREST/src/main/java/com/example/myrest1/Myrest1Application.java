package com.example.myrest1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.modelmapper.ModelMapper;

@SpringBootApplication
public class Myrest1Application {

	public static void main(String[] args) {
		SpringApplication.run(Myrest1Application.class, args);

	}
	
		@Bean
		public ModelMapper modelMapper(){

			return new ModelMapper();
		}
	

}
