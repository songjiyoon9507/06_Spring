package com.home.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

// Bean : 스프링이 만들고 관리하는 객체

@Controller // 요청 / 응답 제어 역할 명시 + Bean 등록
@RequestMapping("param") // 공통주소 param /param 으로 시작하는 모든 요청이 현재 컨트롤러로 매핑
@Slf4j // log 사용할 거라고 알려주는 거 lombok 에서 지원 log 를 이용한 메세지 출력 시 사용 (Lombok 제공)
public class ParameterController {

	@GetMapping("main") // /param/main GET 방식 요청 매핑
	public String paramMain() {
		
		// classpath: src/main/resources
		// 접두사 : classpath:/templates/
		// 접미사 : .html
		// -> src/main/resources/templates/param/param-main.html
		return "param/param-main";
	}
	
	/* 1. HttpServletRequest.getParameter("key") 이용
	 * 
	 * HttpServletRequest :
	 * - 요청 클라이언트 정보, 제출된 파라미터 등을 저장한 객체
	 * - 클라이언트 요청 시 생성
	 * 
	 * 전달인자 해결사  전달인자 Argument 해결사 Resolver
	 * 
	 * ArgumentResolver (전달 인자 해결사)
	 * Spring Boot 가 매개변수 전달인자 보고 알아서 불러줌
	 * - Spring 의 Controller 메서드 작성 시
	 * 매개변수에 원하는 객체를 작성하면
	 * 존재하는 객체를 바인딩 또는 없으면 생성해서 바인딩
	 * */
	
	@PostMapping("test1") // /param/test1 POST 방식 요청 매핑
	public String paramTest1(HttpServletRequest req) {
		
		String inputName = req.getParameter("inputName");
		int inputAge = Integer.parseInt(req.getParameter("inputAge"));
		String inputAddress = req.getParameter("inputAddress");
		
		// debug : 코드 오류 해결
		// -> 코드 오류 없는데 정상 수행이 안될 때
		// -> 값이 잘못된 경우 -> 값 추적
		log.debug("inputName : " + inputName);
		
		// redirect
		/* Spring 에서 Redirect(재요청) 하는 방법
		 * 
		 * -Controller 메서드 반환 값에
		 * "redirect:요청주소"; 작성
		 * */
		return "redirect:/param/main";
	}
}
