package com.home.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.home.todo.model.dto.Todo;
import com.home.todo.model.service.TodoService;

@RequestMapping("todo") // "/todo"로 시작하는 모든 요청 매핑
@Controller
public class TodoController {

	@Autowired // 같은 타입/상속관계 Bean 의존성 주입 (DI)
	private TodoService service;
	
	@PostMapping("add") // "/todo/add" Post 방식 요청 매핑
	public String addTodo(
			@RequestParam("todoTitle") String todoTitle,
			@RequestParam("todoContent") String todoContent,
			RedirectAttributes ra
			) {
		
		// RedirectAttributes : 리다이렉트 시 값을 1회성으로 전달하는 객체
		// RedirectAttributes.addFlashAttribute("key", value) 형식으로 작성하면
		// 잠깐 세션에 속성을 추가 (원래는 request scope)
		
		/* [원리]
		 * 응답 전 : request scope
		 * redirect 중 : session scope 로 이동
		 * 응답 후 : request scope 복귀
		 * */
		
		// 서비스 메서드 호출 후 결과 반환 받기 (insert)
		int result = service.addTodo(todoTitle, todoContent);
		
		// 삽입 결과에 따라 message 값 지정
		String message = null;
		
		if(result > 0) message = "할 일 추가 성공";
		else message = "할 일 추가 실패";
		
		// 리다이렉트 후 1회성으로 사용할 데이터를 속성으로 추가
		ra.addFlashAttribute("message", message);
		
		// 요청 주소
		return "redirect:/"; // 메인 페이지 재요청
	}
	
	// 상세 조회
	@GetMapping("detail")
	public String todoDetail(
			@RequestParam("todoNo") int todoNo,
			Model model,
			RedirectAttributes ra
			) {
		
		Todo todo = service.todoDetail(todoNo);
		
		// 분기 처리
		String path = null;
		
		if(todo != null) { // 조회 결과 있을 경우
			
			// forward : templates/todo/detail.html
			path = "todo/detail";
			
		} else { // 조회 결과 없을 경우
			
			path = "redirect:/"; // 메인 페이지로 리다이렉트
			
			// RedirectAttributes :
			// - 리다이렉트 시 데이터를 request scope -> (잠시) session scope 로
			// 전달할 수 있는 객체 (응답 후 request scope 로 복귀)
			
			ra.addFlashAttribute("message", "해당 할 일이 존재하지 않습니다.");
		}
		
		model.addAttribute("todo", todo);
		
		return path;
	}
}
