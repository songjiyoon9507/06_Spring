package edu.kh.project.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("todo")
@RequiredArgsConstructor
public class TodoMainController {

	@GetMapping("main")
	public String todoMain() {
		return "todo/main";
	}
	
}
