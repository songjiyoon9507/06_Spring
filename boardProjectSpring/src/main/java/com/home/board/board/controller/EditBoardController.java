package com.home.board.board.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.home.board.board.model.dto.Board;
import com.home.board.board.model.service.EditBoardService;
import com.home.board.member.model.dto.Member;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("editBoard")
public class EditBoardController {

	private final EditBoardService service;
	
	@GetMapping("{boardCode:[0-9]+}/insert")
	public String boardInsert(@PathVariable("boardCode") int boardCode) {
		return "board/boardWrite";
	}
	
	@PostMapping("{boardCode:[0-9]+}/insert")
	public String boardInsert(
			@PathVariable("boardCode")int boardCode,
			@ModelAttribute Board inputBoard,
			@SessionAttribute("loginMember") Member loginMember,
			@RequestParam("images") List<MultipartFile> images,
			RedirectAttributes ra) {
		
		// @RequestParam 으로 같은 name 속성 값 가진 애들을 가져올 때
		// 배열, List , Map 으로 다 가져올 수 있음
		// Map 으로 가져올 때는 키 값이 같으면 제일 앞에 하나만 나오기 때문에
		// name 이 같은 값일 때는 배열이나 List 로 받아옴
		// MultipartFile 가져올 때는 List 형태가 적절하기 때문에 List로 받아온 것
		
		
		return "";
	}
}
