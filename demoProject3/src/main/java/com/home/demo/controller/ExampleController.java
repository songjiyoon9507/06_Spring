package com.home.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Bean : 스프링이 만들고 관리하는 객체

@Controller // 요청 / 응답 제어 역할인 Controller 임을 명시 + Bean 등록
public class ExampleController {

	/* 요청 주소를 매핑하는 방법
	 * 
	 * 1) @RequestMapping("주소")
	 * GET / POST 둘 다 매핑됨
	 * 
	 * 2) @GetMapping("주소") : GET (조회) 방식 요청 매핑
	 * 
	 * 	  @PostMapping("주소") : POST (삽입) 방식 요청 매핑
	 * 
	 *    @PutMapping("주소") : PUT (수정) 방식 요청 매핑
	 *    
	 *    @DeleteMapping("주소") : DELETE (삭제) 방식 요청 매핑
	 *    
	 *    HTTP 통신할 때 CRUD
	 *    Rest API 회원 조회 요청할 때 자원을 이름으로 구분해서 자원의 상태를 주고 받는 API
	 *    (웹에서 요청하든 모바일에서 요청하든)
	 *    멱등성 == 항상 똑같은 거 몇 번을 호출하든 똑같은 결과
	 *    멱등성 적용된 방식 GET PUT DELETE 멱등하다고 함 몇 번을 하든 똑같음
	 *    POST 는 결제 2번 요청 보내면 2번 결제됨
	 *    수정은 똑같은 수정 여러 번 보내도 똑같음
	 * */
	
	@GetMapping("example") // /example 이라는 요청이 GET 방식으로 왔을 때 매핑해줌
	public String exampleMethod() {
		
		// return 에는 forward 하려는 html 파일의 경로 작성하는 거
		// 단, ViewResolver 가 제공하는
		// Thymeleaf 접두사, 접미사 제외하고 작성
		
		// 접두사 : classpath:/templates/
		// 접미사 : .html
		return "example"; // src/main/resources/templates/example.html
	}
}
