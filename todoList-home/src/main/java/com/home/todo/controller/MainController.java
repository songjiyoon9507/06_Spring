package com.home.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j // 로그 객체 자동 생성
@Controller // 요청/응답 제어 역할 명시 + Bean 등록
public class MainController {
	
	@RequestMapping("/") // 최상위 요청 들어왔을 때
	public String mainPage() {
		
		// service 호출해서 DB에서 할 일 목록 가져올 거임
		
		
		// 메인페이지로 forward 시켜줄거임
		return "common/main";
		// classpath:/templates/
		// common/main
		// .html
		// -> 이쪽으로 forward 시켜주겠다.
	}
}
