package com.home.board.myPage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	
	/** 프로필 이미지 변경 화면 이동
	 * @return
	 */
	@GetMapping("profile")
	public String profile() {
		return "myPage/myPage-profile";
	}
	
	/** 비밀번호 변경 화면 이동
	 * @return
	 */
	@GetMapping("changePw")
	public String changePw() {
		return "myPage/myPage-changePw";
	}
	
	/** 회원 탈퇴 화면 이동
	 * @return
	 */
	@GetMapping("secession")
	public String secession() {
		return "myPage/myPage-secession";
	}

	/** 회원 정보 수정
	 * @param inputMember : 제출된 회원 닉네임, 전화번호, 주소(,,)
	 * @param loginMember : 로그인한 회원 정보(회원 번호 사용할 예정)
	 * @param memberAddress : 주소만 따로 받은 String[]
	 * @param ra : 리다일렉트 시 request scope 로 데이터 전달
	 * @return redirect:info
	 */
	@PostMapping("info")
	public String updateInfo(Member inputMember,
			@SessionAttribute("loginMember") Member loginMember,
			@RequestParam("memberAddress") String[] memberAddress,
			RedirectAttributes ra
			) {
		
		// 로그인 멤버의 회원 번호를 가져와서 inputMember 에 추가
		int memberNo = loginMember.getMemberNo();
		inputMember.setMemberNo(memberNo);
		
		// 회원 정보 수정 서비스 호출
		int result = service.updateInfo(inputMember, memberAddress);
		
		String message = null;
		
		if(result > 0) {
			message = "회원 정보 수정 성공";
			
			// loginMember 는 세션에 저장된 로그인한 회원 정보가 저장된 객체를 참조
			// DB 에 바뀐 내용대로  loginMember도 바꿔줘야함
			
			loginMember.setMemberNickname(inputMember.getMemberNickname());
			loginMember.setMemberTel(inputMember.getMemberTel());
			loginMember.setMemberAddress(inputMember.getMemberAddress());
			
		} else {
			message = "회원 정보 수정 실패";
		}
		
		ra.addFlashAttribute("message", message);
		
		// myPage/info 로 요청 보냄 (상대경로)
		return "redirect:info";
	}
	
}
