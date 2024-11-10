package com.jpa.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.jpa.springboot.dto.FormData;

@Controller
public class HelloController {

	@RequestMapping("/helloWorld")
	public String helloWorld() {
		return "helloWorld";
	}
	
	@RequestMapping("/thymeleaf-test")
	public String thymeleafTest() {
		return "thymeleaf-test";
	}
	
	@RequestMapping("/thymeleaf-test2")
	public String thymeleafTest2(Model model) {
		
		// View 페이지로 데이터(model)를 같이 전달
		model.addAttribute("data1", "홍길동");
		// "data1" 변수 이름 (key), "홍길동" 값 (value)
		
		model.addAttribute("data2", "조선시대");
		
		return "thymeleaf-test2";
	}
	
	@RequestMapping("/thymeleaf-test3")
	public ModelAndView thymeleafTest3(ModelAndView mav) {
		
		// 1. 데이터를 지정 (key, value)
		mav.addObject("name", "이순신");
		mav.addObject("age", 27);
		
		// 2. 뷰페이지를 지정 (html 파일 이름)
		mav.setViewName("thymeleaf-test3");
		
		// 3. 리턴 mav 리턴해줘야함 return 타입이 String 아님
		// ModelAndView 형태
		return mav;
	}
	
	@RequestMapping("/thymeleaf-test4/{num}")
	public String thymeleafTest4(@PathVariable int num, Model model) {
		
		// 결과 변수
		int result = 0;
		
		// 입력된 변수 num 크기 만큼 반복하면서 합산
		// num 값은 int 외에는 받을 수 없도록 되어있기 때문에 문자열은 오류 발생
		for(int i = 1 ; i <= num ; i++) {
			result += i;
		}
		
		// 뷰페이지로 결과 전달
		model.addAttribute("num", num);
		model.addAttribute("result", result);
		
		System.out.println("변하는 변수 값 num : " + num);
		
		return "thymeleaf-test4";
	}
	
	@RequestMapping("/thymeleaf-test5/{num}")
	public String thymeleafTest5(@PathVariable int num, Model model) {
		
		// 결과 변수
		String str = "";
		
		// 입력된 변수 num에 해당하는 구구단 반복
		for(int i = 1 ; i <= 9 ; i++) {
			str += num + " x " + i + " = " + num * i + "<br>";
		}
		
//		System.out.println(str);
		
		// 뷰 페이지로 결과 전달
		model.addAttribute("str", str);
		model.addAttribute("num", num);

		return "thymeleaf-test5";
	}
	
	@RequestMapping("/thymeleaf-test6/{num}")
	public ModelAndView thymeleafTest6(@PathVariable int num, ModelAndView mav) {
		
		// 결과 변수
		String str = "";
		
		// 입력된 변수 num에 해당하는 구구단 반복
		for(int i = 1 ; i <= 9 ; i++) {
			str += num + " x " + i + " = " + num * i + "<br>";
		}
		
//		System.out.println(str);
		
		mav.addObject("str", str);
		mav.addObject("num", num);
		mav.setViewName("thymeleaf-test6");
		
		return mav;
	}
	
	@GetMapping("/thymeleaf-test7")
	public String thymeleafTest7(Model model) {
		
		model.addAttribute("msg", "아래 폼 값을 입력하고 전송(Send) 버튼을 클릭하세요");
		
		return "thymeleaf-test7";
	}
	
	@PostMapping("/thymeleaf-test7")
	public String thymeleafTest7(@RequestParam("data1") String data1,
			Model model) {
		
		model.addAttribute("msg", "회원님이 입력하신 값은 <span style='color:red'>" + data1 + "</span> 입니다.");
		model.addAttribute("data1", data1);
		
		return "thymeleaf-test7";
	}
	
	@RequestMapping(value="/thymeleaf-test8", method=RequestMethod.GET)
	public ModelAndView formPage(ModelAndView mav) {
		
		mav.addObject("msg", "아래 폼 값을 입력하고 전송(Send) 버튼을 클릭하세요");
		mav.setViewName("thymeleaf-test8");
		
		return mav;
	}
	
	@RequestMapping(value="/thymeleaf-test8", method=RequestMethod.POST)
	public ModelAndView formSend(
			@RequestParam("data1") String data1,
			ModelAndView mav) {
		// RequestParam form 으로부터 넘어오는 값 받아올 때 사용
		
		mav.addObject("msg", "회원님이 입력하신 값은 <span style='color:red'>" + data1 + "</span> 입니다.");
		mav.addObject("data1", data1);
		mav.setViewName("thymeleaf-test8");
		
		return mav;
	}
	
	@RequestMapping(value="/thymeleaf-test9", method=RequestMethod.GET)
	public ModelAndView multiFormPage(ModelAndView mav) {
		
		mav.addObject("msg", "아래 여러 개의 폼 값을 입력하고 전송 버튼을 클릭하세요");
		mav.setViewName("thymeleaf-test9");
		
		return mav;
	}
	
	@RequestMapping(value="/thymeleaf-test9", method=RequestMethod.POST)
	public ModelAndView multiFormSend(
			@RequestParam("id") String id,
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("age") int age,
			@RequestParam("gender") String gender,
			ModelAndView mav) {
		
		mav.addObject("id", id);
		mav.addObject("name", name);
		mav.addObject("email", email);
		mav.addObject("age", age);
		mav.addObject("gender", gender);
		
		mav.setViewName("thymeleaf-test9");
		
		return mav;
	}
	
	@RequestMapping(value="/thymeleaf-test10", method=RequestMethod.GET)
	public ModelAndView multiFormPageDTO(
			@ModelAttribute("formData") FormData formData,
			ModelAndView mav) {
		
		// html에서 formData를 thymeleaf 에서 찾음
		mav.addObject("formData", formData);
		mav.addObject("msg", "아래 여러 개의 폼 값을 입력하고 전송 버튼을 클릭하세요");
		mav.setViewName("thymeleaf-test10");
		
		return mav;
	}
	
	@RequestMapping(value="/thymeleaf-test10", method=RequestMethod.POST)
	public ModelAndView multiFormPageSend(
			@ModelAttribute("formData") FormData formData,
			ModelAndView mav) {
		
			mav.addObject("formData", formData);
			mav.setViewName("thymeleaf-test10");
		
		return mav;
	}
	
	@RequestMapping(value="/thymeleaf-test11", method=RequestMethod.GET)
	public ModelAndView formPageUtil(ModelAndView mav) {
		
		mav.addObject("msg", "Hello World");
		mav.setViewName("thymeleaf-test11-util");
		
		return mav;
	}
}
