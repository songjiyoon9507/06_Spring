package edu.kh.project.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.todo.model.service.TodoService;
import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("todo")
@RequiredArgsConstructor
public class TodoMainController {

	private final TodoService service;
	
	@GetMapping("main")
	public String todoMain() {
		return "todo/main";
	}
	
	@PostMapping("main")
	public String addTodo(@RequestParam("uploadFile") MultipartFile uploadFile,
			RedirectAttributes ra
			) {
		
		String path = service.fileUpload(uploadFile);
		
		return "todo/main";
	}
}
