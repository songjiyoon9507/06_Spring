package com.jpa.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
