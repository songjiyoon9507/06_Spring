package com.home.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.home.todo.model.dto.Todo;
import com.home.todo.model.service.TodoService;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("todo") // "/todo"로 시작하는 모든 요청 매핑
@Controller
@Slf4j
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
		
		log.debug("todoNo="+todoNo);
		
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
	
	// 완료 여부 변경 location.href 는 GET 요청
	/** 완료 여부 변경
	 * @param todo : 커맨드 객체 (@ModelAttribute 생략)
	 * 				- todoNo, complete 두 필드가 세팅된 상태
	 * @return
	 */
	@GetMapping("changeComplete")
	public String changeComplete(Todo todo,
			RedirectAttributes ra) {
		// RequestParam 으로도 얻어올 수 있지만 ModelAttribute 로 얻어와서 Todo 에 바로 세팅
		// @ModelAttribute 는 생략 가능
		// js 에서 넘겨준 값이 todoNo, complete 쿼리스트링으로 넘겨줌
		
		// 변경 서비스 호출
		int result = service.changeComplete(todo);
		
		// 변경 성공 시 : "변경 성공"
		// 변경 실패 시 : "변경 실패"
		
		String message = null;
		
		if(result > 0) message = "변경 성공";
		else message = "변경 실패";
		
		// 현재 요청 주소 : /todo/changeComplete
		// 응답 주소 : /todo/detail
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:detail?todoNo=" + todo.getTodoNo(); // 상대경로
		// 상대 경로 작성이라서 redirect:/ 이렇게 작성하면 localhost/detail 이 요청됨
		// 그런 경로는 존재하지 않음
	}
	
	/** 수정 화면 전환
	 * @return todo/update 로 forward
	 */
	@GetMapping("update")
	public String todoUpdate(@RequestParam("todoNo") int todoNo,
			Model model) {

		// 상세 조회 서비스 호출하면 수정화면에 출력할 이전 내용을 쓸 거임

		// todoNo 를 전달하면 상세 조회해주는 서비스 재활용
		Todo todo = service.todoDetail(todoNo);
		
		model.addAttribute("todo", todo);
		
		// forward 시켜줄 거임
		return "todo/update";
	}
	
	// 오버로딩 적용 매개변수 타입, 개수 다르면 오버로딩
	/** 할 일 수정
	 * @param todo : 커맨드 객체 (전달 받은 파라미터가 자동으로 DTO의 필드에 세팅된 객체)
	 * @param ra
	 * @return 
	 */
	@PostMapping("update")
	public String todoUpdate(Todo todo, RedirectAttributes ra) {
		// ModelAttribute 사용해서 가져온 거임
		// redirect 해줄 거라서 RedirectAttributes 얻어옴
		
		// 수정 서비스 호출 (update 할 거)
		int result = service.todoUpdate(todo);
		
		// 분기 처리
		String path = "redirect:";
		String message = null;
		
		if(result > 0) {
			// 업데이트 성공시
			// 상세 조회로 리다이렉트
			path += "/todo/detail?todoNo=" + todo.getTodoNo();
			message = "수정 성공";
		} else {
			// 다시 수정 화면으로 리다이렉트
			path += "/todo/update?todoNo=" + todo.getTodoNo();
			message = "수정 실패";
		}
		
		ra.addFlashAttribute("message", message);
		
		return path;
	}
}
