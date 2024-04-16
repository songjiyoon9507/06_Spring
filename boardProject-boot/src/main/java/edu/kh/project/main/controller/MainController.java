package edu.kh.project.main.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.service.MemberService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MainController {

	private final MemberService service;
	
	@RequestMapping("/") // "/" 요청 매핑(method 가리지 않음)
	public String mainPage(Model model) {
		// forward redirect
		// 접두사/접미사 제외한 경로 작성
		// templates 안에 common 폴더 안에 main.html
		
		// DB 조회해서 email List로 가져오기
		List<Member> memberList = service.memberList();
		
		// request scope 에 memberList 세팅
		model.addAttribute("memberList", memberList);
		
		return "common/main";
	}
}
