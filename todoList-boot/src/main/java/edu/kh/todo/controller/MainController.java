package edu.kh.todo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.todo.model.dto.Todo;
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
	public String mainPage(Model model) {
		
		// Model 사용하던가 HttpServletRequest req 로 보내주면 됨
		
		// 의존성 주입(DI) 확인 (진짜 Service 객체 들어옴)
		log.debug("service : " + service);
//		service : edu.kh.todo.model.service.TodoServiceImpl@742dd7e0
		
		// Service 메서드 호출 후 결과 반환 받기
		Map<String, Object> map = service.selectAll();
		
		// map 에 담긴 내용 추출 다운캐스팅 해줘야함 Map 에서 Object 형태로 받아왔기 때문에
		// List 형태롤 형변환해서 받아놓음
		List<Todo> todoList = (List<Todo>) map.get("todoList");
		int completeCount = (int) map.get("completeCount");
		
		// Model : 데이터 전달용 객체 (request scope) + session 변환 가능
		model.addAttribute("todoList", todoList);
		model.addAttribute("completeCount", completeCount);
		
		// classpath:/templates/
		// .html
		// -> 이쪽으로 forward
		return "common/main";
	}
}
