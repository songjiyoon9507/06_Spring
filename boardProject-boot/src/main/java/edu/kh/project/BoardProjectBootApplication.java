package edu.kh.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// Spring Security 에서 기본 제공하는 로그인 페이지 이용 안하겠다는 코드 추가 (이 코드 추가하면 Whitelabel error 뜸)
@SpringBootApplication(exclude= {SecurityAutoConfiguration.class})
public class BoardProjectBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardProjectBootApplication.class, args);
	}

}