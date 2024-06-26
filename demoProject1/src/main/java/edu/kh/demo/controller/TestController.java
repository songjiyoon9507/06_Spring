package edu.kh.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// Java 객체 : new 연산자에 의해 Heap 영역에
//             클래스에 작성된 내용대로 생성된 것

// instance : 개발자가 만들고 관리하는 객체

// IOC 와 DI 개발자가 객체 만들고 생명주기 관리하는 게 아니라 Spring 이 해줌
// Bean : Spring Container(Spring)가 만들고 관리하는 객체

@Controller // 요청/응답을 제어할 컨트롤러 역할 명시 + Bean 으로 등록해주는 어노테이션(== 객체로 생성해서 스프링이 관리)
public class TestController {
	// servlet 할 때는 extends HttpServlet @WebServlet("/test") 어노테이션 했는데
	// Spring 은 방법이 다름
	
	// 기존 Servlet : 클래스 단위로 하나의 요청만 처리 가능
	// Spring : 메서드 단위로 요청 처리 가능
	
	// index.html /test 요청 받는 방법
	// @RequestMapping("요청주소")
	// - 요청 주소를 처리할 메서드를 매핑하는 어노테이션 (클래스 단에서도 사용할 수 있음)
	
	/* RequestMapping
	 * 1) 메서드에 작성
	 * - 요청주소와 메서드를 매핑
	 * - GET/POST 가리지 않고 매핑 (속성을 통해서 지정 가능)
	 * @RequestMapping(value="test" , method=RequestMethod.GET)
	 * (잘 사용하지 않음)
	 * 앞에 / 붙이지 않음 (특수한 몇몇 경우를 제외하고는 붙이지 않음)
	 * @RequestMapping("test")
	 * 
	 * 2) 클래스에 작성
	 * - 공통 주소를 매핑
	 * ex) /todo/insert, /todo/select, /todo/update 이런 요청들이 있다고 치면
	 * 공통된 주소 /todo
	 * todo 라는 공통된 요청으로 받아서 한곳에서 처리(공통주소까지만 작성)
	 * 
	 * @RequestMapping("todo")
	 * @Controller
	 * public class TodoController {
	 * 		
	 *		@RequestMapping("insert")	
	 * 		public String insert() {}
	 * 		=> /todo/insert 매핑
	 * 	
	 *		@RequestMapping("select")
	 * 		public String select() {}
	 * 		=> /todo/select 매핑
	 * 
	 * 		@RequestMapping("update")
	 * 		public String update() {}
	 * 		=> /todo/update 매핑
	 * 
	 * }
	 * */
	
	/* Spring Boot Controller 에서
	 * 특수한 경우를 제외하고
	 * 매핑 주소 제일 앞에 "/"를 작성 안함
	 * (관례)
	 * */
	
	@RequestMapping("test") // /test 요청 시 처리할 메서드 매핑(GET/POST 가리지 않고)
	public String testMethod() {
		System.out.println("/test 요청 받음");
		
		/* 반환형 String 기본
		 * Controller 메서드의 반환형이 String 인 이유
		 * - 메서드에서 반환되는 문자열이
		 *   forward 할 HTML 파일 경로가 되기 때문
		 * */
		
		/*
		 * Thymeleaf : JSP 대신 사용하는 템플릿 엔진
		 * 
		 * Thymeleaf 에서 정해둔 접두사와 접미사가 있음
		 * classpath: == src/main/resources
		 * 접두사 : classpath:/templates/
		 * 접미사 : .html
		 * */
		
		// src/main/resources/templates/	.html
		// 빈칸에 들어올 값만 작성해주면 됨
		return "test"; // forward(접두사 + 반환값 + 접미사 경로의 HTML 파일로 forward)
		// 반환될 경로만 작성
		// src/main/resources/templates/test.html
		
		// templates 폴더 안에 test.html 파일 만들면 됨
		// spring boot 는 서버 껐다 켰다 안해도 됨
		
		// 수정
	}
}
