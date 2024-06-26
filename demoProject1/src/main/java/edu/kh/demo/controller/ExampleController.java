package edu.kh.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Bean : 객체를 만드는 것부터 관리하는 것까지 Spring 이 하는 거
// 스프링이 만들고 관리하는 객체

@Controller // 요청 / 응답 제어 역할인 Controller 임을 명시 + Bean 등록
public class ExampleController {
	
	/* 요청 주소 매핑하는 방법
	 * 
	 * 1) @RequestMapping("주소")
	 * 
	 * 2) @GetMapping("주소") : GET (조회) 방식 요청 매핑
	 * 
	 * 	  @PostMapping("주소") : POST (삽입) 방식 요청 매핑
	 * 
	 *    @PutMapping("주소") : PUT (수정) 방식 요청 매핑
	 *    
	 *    @DeleteMapping("주소") : DELETE (삭제) 방식 요청 매칭
	 *    
	 *    HTTP 통신 CRUD
	 *    create read update delete
	 *    
	 *    PC 에서 보내든 모바일로 보내든 똑같은 요청
	 *    REST API 자원을 이름으로 구분해서 자원의 상태를 주고받는 거 (Put 이나 Delete)
	 *    (멱등) 항상 똑같은 결과 get put delete
	 *    
	 *    post 는 다른 결과 ex) 결제 2번 됨 
	 * */
	
//	@RequestMapping("example")
	@GetMapping("example") // /example GET 방식 요청 매핑 (POST는 못 받음)
	public String exampleMethod() {
		// a 태그는 get 요청
		
		// forward 하려는 HTML 파일 경로 작성
		// 단, ViewResolver 가 제공하는 Thymeleaf 접두사, 접미사 제외하고 작성
		
		// 접두사 : classpath:/templates/
		// 접미사 : .html
		return "example"; // src/main/resources/templates/example.html
	}
}
