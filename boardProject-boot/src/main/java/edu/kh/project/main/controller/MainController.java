package edu.kh.project.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

	@RequestMapping("/") // "/" 요청 매핑(method 가리지 않음)
	public String mainPage() {
		// forward redirect
		// 접두사/접미사 제외한 경로 작성
		// templates 안에 common 폴더 안에 main.html
		return "common/main";
	}
}
