package com.test.project.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

	// 모든 localhost 페이지 요청에 메인 페이지로 이동
	@RequestMapping("/")
	public String main() {
		return "common/main";
	}
	
}
