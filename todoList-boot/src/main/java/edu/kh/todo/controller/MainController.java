package edu.kh.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.todo.model.service.TodoService;
import lombok.extern.slf4j.Slf4j;

@Slf4j // 로그 객체 자동 생성
@Controller // 요청 / 응답 제어 역할 명시 + Bean 등록
public class MainController {
	
	// Controller -> Service -> Mapper -> DB
	
//	private TodoService service = new TodoServiceImpl(); 이전에는 이렇게 사용했었음
	@Autowired // DI (의존성 주입) 객체가 만들어져서 service 안에 들어온거임
	private TodoService service;
	
	@RequestMapping("/") // 메인페이지 요청 최상위
	public String mainPage() {
		
		// 의존성 주입(DI) 확인 (진짜 Service 객체 들어옴)
		log.debug("service : " + service);
//		service : edu.kh.todo.model.service.TodoServiceImpl@742dd7e0
		
		// classpath:/templates/
		// .html
		// -> 이쪽으로 forward
		return "common/main";
	}
}
