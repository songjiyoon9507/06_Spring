package com.home.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/* Java 객체 : instance new 연산자에 의해 Heap 영역에
 *             클래스에 작성된 내용대로 생성된 것
 * 
 * instance : 개발자가 만들고 관리하는 객체
 * 
 * IOC 제어반전
 * 
 * Bean : Spring Container(Spring)가 만들고 관리하는 객체
 * */

@Controller // 요청/응답을 제어할 컨트롤러 역할 명시 + Bean 으로 등록 (== 객체로 생성해서 스프링이 관리)
public class TestController {
	// 기존 Servlet : 클래스 단위로 하나의 요청만 처리 가능
	// Spring : 메서드 단위로 요청 처리 가능
	
	// @RequestMapping("요청주소")
	// - 요청 주소를 처리할 메서드를 매핑하는 어노테이션
	// 메서드에서만 사용하는 어노테이션은 아님 클래스 단에서도 사용 가능
	
	/* 1) 메서드에 작성 :
	 * - 요청주소와 메서드를 매핑
	 * - GET/POST 가리지 않고 매핑 (속성을 통해서 지정 가능)
	 * 
	 * 2) 클래스에 작성
	 * - 공통 주소를 매핑
	 *  ex) /todo/insert, /todo/select, /todo/update
	 * */
	
	/* 
	 * @RequestMapping("todo")
	 * @Controller
	 * public class TodoController {
	 * 		
	 * 		@RequestMapping("insert") // todo/insert 매핑
	 * 		public String insert() {}
	 * 
	 * 		@RequestMapping("select") // todo/select 매핑
	 * 		public String select() {}
	 * 
	 * 		@RequestMapping("update") // todo/update 매핑
	 * 		public String update() {}
	 * 
	 * }
	 * 
	 * */
	/***************************************************************/
	// Spring Boot Controller 에서
	// 특수한 경우를 제외하고
	// 매핑 주소 제일 앞에 "/" 를 작성 안함
	// 관례대로 이용
	/***************************************************************/
	
	// @RequestMapping(value="test" , method=RequestMethod.GET) 이렇게 써줄 수도 있지만 잘 안 씀
	// -> @GetMapping 이렇게 쓸 수 있어서
	
	@RequestMapping("test") // /test 요청 시 처리할 메서드 매핑(GET/POST 가리지 않고)
	public String testMethod() {
		System.out.println("/test 요청 받음");
		
		/* Controller 메서드의 반환형이 String 인 이유
		 * - 메서드에서 반환되는 문자열이
		 *   forward 할 HTML 파일 경로가 되기 때문
		 * */
		
		/* 
		 * Thymeleaf : JSP 대신 사용하는 템플릿 엔진
		 * 
		 * Thymeleaf 가 정해둔 접미사와 접두사
		 * classpath: == src/main/resources
		 * 접두사 : classpath:/templates
		 * 접미사 : .html
		 * */
		
		// src/main/resources/templates/	.html
		// src/main/resources/templates/test.html
		return "test"; // forward(접두사 + 반환값 + 접미사 경로의 html 로 forward)
	}
}
