package edu.kh.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

// Spring Security 에서 기본 제공하는 로그인 페이지 이용 안하겠다는 코드 추가 (이 코드 추가하면 Whitelabel error 뜸)
@EnableScheduling // 스프링 스케줄러를 이용하기 위한 활성화 어노테이션
@SpringBootApplication(exclude= {SecurityAutoConfiguration.class})
public class BoardProjectBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardProjectBootApplication.class, args);
	}

}
