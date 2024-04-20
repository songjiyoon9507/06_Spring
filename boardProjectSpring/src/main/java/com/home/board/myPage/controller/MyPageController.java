package com.home.board.myPage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.home.board.member.model.dto.Member;
import com.home.board.myPage.model.service.MyPageService;

import lombok.RequiredArgsConstructor;

@SessionAttributes({"loginMember"})
@Controller
@RequestMapping("myPage")
@RequiredArgsConstructor
public class MyPageController {

	private final MyPageService service;
	
	/** 내 정보 조회/수정 화면으로 전환
	 * @param loginMember : 세션에 존재하는 loginMember를 얻어와 매개변수에 대입
	 * @param model : 데이터 전달용 객체 (기본 request scope)
	 * @return myPage/myPage-info 로 요청 위임
	 */
	@GetMapping("info")
	public String info(
			@SessionAttribute("loginMember") Member loginMember,
			Model model
			) {
		
		// 이동하자마자 보여줘야하는 내용들 (현재 로그인한 사람의 정보)
		
		// session 에 있는 loginMember 정보 얻어오기
		// @SessionAttribute("loginMember") Member loginMember
		// @SessionAttributes({"loginMember"})
		
		// 주소만 꺼내옴
		String memberAddress = loginMember.getMemberAddress();
		
		// 주소 있을 경우에만 동작
		if(memberAddress != null) {
			
			// 구분자 "^^^"를 기준으로
			// memberAddress 값을 쪼개어 String[] 로 반환
			String[] arr = memberAddress.split("\\^\\^\\^");
			// "^^^" 안 먹힘, "\^\^\^" 이것도 안 먹힘
			// regex 정규표현식이라서 그냥 문자열 안 먹힘
			
			// "08350^^^서울 구로구 천왕동 2-19^^^천왕"
			// -> ["08350", "서울 구로구 천왕동 2-19", "천왕"]
			model.addAttribute("postcode", arr[0]);
			model.addAttribute("address", arr[1]);
			model.addAttribute("detailAddress", arr[2]);
		}
		return "myPage/myPage-info";
	}
}
