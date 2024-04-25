package com.home.board.board.controller;

import java.io.IOException;
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
			RedirectAttributes ra) throws IllegalStateException, IOException {
		
		// @RequestParam 으로 같은 name 속성 값 가진 애들을 가져올 때
		// 배열, List , Map 으로 다 가져올 수 있음
		// Map 으로 가져올 때는 키 값이 같으면 제일 앞에 하나만 나오기 때문에
		// name 이 같은 값일 때는 배열이나 List 로 받아옴
		// MultipartFile 가져올 때는 List 형태가 적절하기 때문에 List로 받아온 것
		
		// inputBoard 안에는 boardTitle, boardContent 담아옴
		// 여기에 boardCode 와 loginMember 에 있는 memberNo setting
		inputBoard.setBoardCode(boardCode);
		inputBoard.setMemberNo(loginMember.getMemberNo());
		
		// Board 테이블 과 boardImg 는 다른 테이블이라서 서비스 insert 2번 해야함
		// boardImg가 board 의 pk 를 foreign key 로 삼음 (boardNo)
		// board 테이블 insert 후 생성된 시퀀스 넘버가 boardNo 인데
		// 그 값을 boardImg insert 할 때 써야함
		// boardNo 로 돌려받는 이유는 insert 후 바로 상세 조회 페이지 보여주기 위해서
		int boardNo = service.boardInsert(inputBoard, images);
		
		String path = null;
		String message = null;
		
		if(boardNo > 0) {
			path = "/board/" + boardCode + "/" + boardNo; // 상세 조회 /board/1/2000
			message = "게시글이 작성 되었습니다.";
		} else {
			path = "insert";
			message = "게시글 작성 실패";
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:" + path;
	}
}
