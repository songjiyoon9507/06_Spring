package com.home.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class DemoProject3Application {

	public static void main(String[] args) {
		SpringApplication.run(DemoProject3Application.class, args);
	}

}

/* main 메서드 안에서 spring application run 실행
 * 
 * @SpringBootApplication 어노테이션 눌러보면 @ComponentScan 있음
 * 
 * @ComponentScan
 * 
 * @Service 어노테이션 보면 @ComponentScan 이 Bean 으로 만들어줌
 * 
 * src/main/java 안에 templates static application.properties 자동으로 생성되어 있음
 * 
 * templates 안에는 HTML 응답페이지 모아둠 thymeleaf 사용 jsp 사용 안함
 * 
 * static 안에는 resources 안에 css, js, image 모아둘 거
 * 
 * 프로젝트의 전반적 설정 적용하는 곳 : application.properties
 * */
