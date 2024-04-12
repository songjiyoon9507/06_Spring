package com.home.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.home.todo.model.service.TodoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j // 로그 객체 자동 생성
@Controller // 요청/응답 제어 역할 명시 + Bean 등록
public class MainController {
	
	// 이전에는 이렇게 직접 객체화해서 사용
//	private TodoService service = new TodoService();
	
	@Autowired // DI (의존성 주입)
	private TodoService service;
	
	@RequestMapping("/") // 최상위 요청 들어왔을 때
	public String mainPage() {
		
		// 의존성 주입(DI) 확인 (진짜 Service 객체 들어옴)
		log.debug("service : " + service);
//		service : com.home.todo.model.service.TodoServiceImpl@5fbc2c58
		
		// service 호출해서 DB에서 할 일 목록 가져올 거임
		
		
		// 메인페이지로 forward 시켜줄거임
		return "common/main";
		// classpath:/templates/
		// common/main
		// .html
		// -> 이쪽으로 forward 시켜주겠다.
		
	}
}
