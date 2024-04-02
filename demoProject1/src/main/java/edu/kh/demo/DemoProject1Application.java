package edu.kh.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication // Spring Boot Application 에 필요한 필수 어노테이션을 모아둔 어노테이션
public class DemoProject1Application {
	// 프로젝트명Application main 메서드 있고 run 메서드 이용해서 실행시킴
	public static void main(String[] args) {
		SpringApplication.run(DemoProject1Application.class, args);
	}
	// @ComponentScan Bean 으로 만들 거 Scanning
	// @Controller 라고 써있는 거 보고 Controller Bean 만들어주고 Controller 역할인 거 읽음
	// => 그 때부터 Spring Container 가 관리하는 거
}
