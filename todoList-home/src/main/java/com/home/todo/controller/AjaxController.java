package com.home.todo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.home.todo.model.dto.Todo;
import com.home.todo.model.service.TodoService;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Controller // 요청/응답을 제어 역할 명시 + Bean 등록
@Slf4j
@RequestMapping("ajax")
public class AjaxController {

	// @Autowired
	// - 등록된 Bean 중 같은 타입 또는 상속관계인 Bean 을
	//   해당 필드에 의존성 주입(DI)
	
	@Autowired
	private TodoService service;
	
	@GetMapping("main") // /ajax/main GET 요청 매핑
	public String ajaxMain() {
		
		// 접두사 : classpath:templates/
		// 접미사 : .html
		return "ajax/main";
	}
	
	// 전체 Todo 개수 조회
	@GetMapping("totalCount")
	@ResponseBody // 동기요청할 때는 return 자리에 forward/redirect 주소 적음
	public int getTotalCount() {
		
		// 전체 할 일 개수 조회 서비스 호출 및 응답
		return service.getTotalCount();
		// 비동기 요청은 return 자리에 돌려 받은 값 그대로 돌려주겠다고 @ResponseBody 적어서
		// 그대로 돌려보냄
	}
	
	/* @ResponseBody
	 * - 컨트롤러 메서드의 반환값을 HTTP 응답 본문에 직접 바인딩하는 역할임을 명시
	 * 
	 * - 컨트롤러 메서드의 반환값을 비동기 요청했던 HTML/JS 파일 부분에
	 *   값을 돌려보내 것이다 명시
	 *   
	 * - @ResponseBody 어노테이션 붙어있으면 forward/redirect 로 인식하지 않음
	 **/
	
	@ResponseBody
	@GetMapping("completeCount")
	public int getCompleteCount() {
		return service.getCompleteCount();
	}
	
	@ResponseBody // 비동기 요청 결과로 값 자체를 반환
	@PostMapping("add")
	public int addTodo(@RequestBody Todo todo) { // 요청 body 에 담긴 값을 Todo 에 저장
		// JSON 이 파라미터로 전달된 경우 @RequestParam("todoTitle") String todoTitle,
		// @RequestParam("todoContent") String todoContent 이런 방법으로 얻어오기 불가능
		return service.addTodo(todo.getTodoTitle(), todo.getTodoContent());
		// Todo 처럼 마땅한 DTO  없을 때는 Map<Stirng, Object> 이런 식으로 받으면 됨
	}
	
	/* @RequestBody
	 * - 비동기 요청(ajax) 시 전달되는 데이터 중
	 *   body 부분에 포함된 요청 데이터를
	 *   알맞은 java 객체 타입으로 바인딩하는 어노테이션
	 *   
	 * -> 비동기 요청 시 body 에 담긴 값을
	 *    알맞은 타입으로 변환해서 매개변수에 저장하는 어노테이션
	 *    
	 * todoTitle, todoContent 필드명 가지고 있는 Todo DTO 있음
	 * 필드명과 JSON 객체 KEY 같으면 value 값을 알아서 Setting 해줌
	 * 
	 * Spring 에는 HttpMessageConverter 가 있음
	 * Spring 에서 비동기 통신 시
	 * - 전달되는 데이터의 자료형
	 * - 응답하는 데이터의 자료형
	 * 두 가지를 알맞은 형태로 가공(변환)해주는 객체
	 * 
	 * - 문자열, 숫자 <-> TEXT
	 * - DTO <-> JSON
	 * - Map <-> JSON
	 * 
	 * (참고)
	 * HttpMessageConverter 가 동작하기 위해서는
	 * Jackson-data-bind 라이브러리가 필요한데
	 * Spring Boot 모듈에 내장되어 있음
	 * (Jackson : 자바에서 JSON 다루는 방법 제공하는 라이브러리)
	 * 
	 * Spring Legacy 는 직접 추가해줘야함
	 * */
	
	@ResponseBody
	@GetMapping("selectList")
	public List<Todo> selectList() {
		
		List<Todo> todoList = service.selectList();
		return todoList;
		
		// List (Java 전용 타입) 를 반환
		// -> JS가 인식할 수 없기 때문에
		// HttpMessageConverter 가
		// JSON 형태로 변환하여 반환해줄 거임
		// -> [{}, {}, {}] JSONArray
	}
}
