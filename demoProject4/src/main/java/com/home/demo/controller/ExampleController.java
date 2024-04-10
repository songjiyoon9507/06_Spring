package com.home.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.home.demo.model.dto.Student;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("example") // /example 로 시작하는 주소를 해당 컨트롤러에 매핑 (공통 주소 매핑)
@Controller // 요청/응답 제어 역할 명시 + Bean 등록
@Slf4j // lombok 라이브러리가 제공하는 log 객체 자동 생성해주는 어노테이션
public class ExampleController {

	/* Model
	 * - Spring 에서 데이터 전달 역할을 하는 객체
	 * 
	 * - org.springframework.ui 패키지
	 * 
	 * - 기본 scope : request
	 * 
	 * - @SessionAttributes 와 함께 사용 시 session scope 변환
	 * 
	 * [기본 사용법]
	 * Model.addAttribute("key", value);
	 * */
	
	// /example/ex1 GET 방식 요청 매핑
	@GetMapping("ex1")
	public String ex1(HttpServletRequest req, Model model) {
		// argument resolver 가 바인딩 시켜줌
		
		/* scope 내장객체 범위
		 * page < request < session < application
		 * */
		
		// request scope
		req.setAttribute("test1", "HttpServletRequest로 전달한 값");
		model.addAttribute("test2", "Model로 전달한 값");
	
		// 단일 값(숫자, 문자열) Model 을 이용해서 html 로 전달
		model.addAttribute("productName", "종이컵");
		model.addAttribute("price", 2000);
		
		// 복수 값(배열, List) Model 을 이용해서 html 로 전달
		List<String> fruitList = new ArrayList<>();
		fruitList.add("사과");
		fruitList.add("딸기");
		fruitList.add("바나나");
		
		model.addAttribute("fruitList", fruitList);
		
		// DTO 객체 Model 을 이용해서 html 로 전달
		Student std = new Student();
		std.setStudentNo("12345");
		std.setName("홍길동");
		std.setAge(22);
		
		model.addAttribute("std", std);
		
		// List<Student> 객체 Model 을 이용해서 html 로 전달
		List<Student> stdList = new ArrayList<>();
		
		stdList.add(new Student("11111", "김일번", 20));
		stdList.add(new Student("22222", "김이번", 21));
		stdList.add(new Student("33333", "김삼번", 22));
		
		model.addAttribute("stdList", stdList);
		
		return "example/ex1"; // templates/example/ex1.html 요청 위임
	}
	
	@PostMapping("ex2") // /example/ex2 POST 방식 매핑
	public String ex2(Model model) {
		// 요청 보낼 때 html 에서 name 속성값으로 보냄
		// request scope -> inputName="홍길동", inputAge=20, color=[Red, Green, Blue]
		
		model.addAttribute("str", "<h1>테스트 중 &times; </h1>");
		
		return "example/ex2";
		// 요청 받은 내용을 다시 위임했을 때 값을 그대로 사용할 수 있음
	}
}
