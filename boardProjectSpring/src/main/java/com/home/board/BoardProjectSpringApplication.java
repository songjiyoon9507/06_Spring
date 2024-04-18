package com.home.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class BoardProjectSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardProjectSpringApplication.class, args);
	}

}
