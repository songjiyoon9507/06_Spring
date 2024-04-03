package edu.kh.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.demo.model.dto.Student;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller // 요청/응답 제어 역할 명시 + Bean 등록
@RequestMapping("example") // /example 로 시작하는 주소를 해당 컨트롤러에 매핑
@Slf4j // lombok 라이브러리가 제공하는 로그 객체 자동생성 어노테이션
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
		
		// scope 내장객체 범위
		// page < request < session < application
		
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
		stdList.add(new Student("22222", "최이번", 20));		
		stdList.add(new Student("33333", "홍삼번", 20));
		
		model.addAttribute("stdList", stdList);
		
		return "example/ex1"; // templates/example/ex1.html 요청 위임
	}
	
	@PostMapping("ex2") // /example/ex2 POST 방식 매핑
	public String ex2(Model model) {
		
		// request -> inputName="홍길동", inputAge=20, color=[Red, Green, Blue]
		
		model.addAttribute("str", "<h1>테스트 중 &times; </h1>");
		// &times; html 에서 해석되면 x 로 나옴
		return "example/ex2";
	}
	
	// 타임리프 사용하려면 html 파일에서 xmlns:th 써줘야함
	
	@GetMapping("ex3")
	public String ex3(Model model) {
		
		// Model : Spring 에서 사용하는 데이터 전달용 객체 (request scope)
		
		model.addAttribute("boardNo", 10);
		
		model.addAttribute("key", "제목");
		
		model.addAttribute("query", "검색어");
		
		return "example/ex3";
	}
	
	/* @PathVariable
	 * - 주소 중 일부분을 변수 값처럼 사용
	 * + 해당 어노테이션으로 얻어온 값은 request scope 에 세팅
	 * 
	 * */
	
	@GetMapping("ex3/{number}")
	public String pathVariableTest(
			@PathVariable("number") int number) {
			// 주소 중 {number} 부분의 값을 가져와 매개변수에 저장
			// + request scope 에 세팅
		
		log.debug("number : " + number);
		// log 에 number : 1 / number : 2 / number : 3 이런 식으로 찍힘
		// html 에서 넘겨줄 때 값 a 태그 안 href 에 /example/ex3/1 2 3 이런식
		
		return "example/testResult";
	}
	
	@GetMapping("ex4")
	public String ex4(Model model) {
		
		Student std = new Student("67890", "잠만보", 22);
		
		model.addAttribute("std", std);
		
		model.addAttribute("num", 100);
		
		return "example/ex4";
	}
	
	@GetMapping("ex5")
	public String ex5(Model model) {
		
		// Model : Spring 에서 값 전달 역할을 하는 객체
		// 기본적으로 request scope + session 으로 확장 가능
		
		model.addAttribute("message", "타임리프 + javascript 사용 연습");
		model.addAttribute("num1", 12345);
		
		Student std = new Student();
		std.setStudentNo("22222");
		model.addAttribute("std", std);
		
		return "example/ex5";
	}
}
