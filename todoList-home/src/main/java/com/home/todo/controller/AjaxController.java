package com.home.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
