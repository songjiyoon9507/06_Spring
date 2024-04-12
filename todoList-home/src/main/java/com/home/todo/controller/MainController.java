package com.home.todo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.home.todo.model.dto.Todo;
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
	public String mainPage(Model model) {
		// Spring 이 관리 하는 Model ArgumentResolver 가 주입해줌
		
		// 의존성 주입(DI) 확인 (진짜 Service 객체 들어옴)
		log.debug("service : " + service);
//		service : com.home.todo.model.service.TodoServiceImpl@5fbc2c58
		
		// service 호출해서 DB에서 할 일 목록 가져올 거임
		// Todo 목록
		// 완료된 개수
		// -> 2개 sql 조회
		
		// Service 메서드 호출 후 결과 반환 받기
		Map<String, Object> map = service.selectAll();
		// 목록 조회 , 카운트 조회 -> Map 으로 가져올 거
		
		// map 에 담긴 내용 추출
		List<Todo> todoList = (List<Todo>) map.get("todoList");
		// Map 에 value 가 Object 형이어서 List<Todo> 형태로 다운캐스팅 해줌
		int completeCount = (int)map.get("completeCount");
		
		// request scope 에 세팅해서 보내줄 거임
		// Model : 값 전달용 객체(request scope) + session 변환 가능
		model.addAttribute("todoList", todoList);
		model.addAttribute("completeCount", completeCount);
		
		// 메인페이지로 forward 시켜줄거임
		return "common/main";
		// classpath:/templates/
		// common/main
		// .html
		// -> 이쪽으로 forward 시켜주겠다.
		
	}
}
