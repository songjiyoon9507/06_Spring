package com.home.board.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.home.board.member.model.dto.Member;
import com.home.board.member.model.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SessionAttributes({"loginMember"})
@Controller
@Slf4j
@RequestMapping("member")
public class MemberController {

	@Autowired
	private MemberService service;
	
	/* [로그인]
	 * - 특정 사이트에 아이디/비밀번호 등을 입력해서
	 *   해당 정보가 있으면 조회/서비스 이용
	 *   
	 * - 로그인 한 정보를 session 에 기록하여
	 *   로그아웃 또는 브라우저 종료 시 까지
	 *   해당 정보를 계속 이용할 수 있게 함
	 * */
	
	/** 로그인
	 * @param inputMember : 커맨드 객체(@ModelAttribute 생략)
	 * 						(memberEmail, memberPw 세팅된 상태)
	 * @param ra : 리다이렉트 시 request scope 로 데이터를 전달하는 객체
	 * @param model : 데이터 전달용 객체(기본 request scope)
	 * @return "redirect:/"
	 */
	@PostMapping("login")
	public String login(Member inputMember,
			RedirectAttributes ra,
			Model model) {
		// 로그인 서비스 호출
		Member loginMember = service.login(inputMember);
		
		// 아이디나 비밀번호가 틀렸을 경우 loginMeber 에 null 들어가 있음
		
		// 로그인 실패 시
		if(loginMember == null) {
			ra.addFlashAttribute("message", "아이디 또는 비밀번호가 일치하지 않습니다.");
		}
		
		// 로그인 성공 시
		if(loginMember != null) {
			// loginMember 를 session scope 에 올려둘 거임
			model.addAttribute("loginMember", loginMember);
			// request scope 에 올려둔 거
			// Controller 상단에 어노테이션 추가로 Session scope 에 setting 완료
		}
		
		// @SessionAttributes ({"key", "key", ...})
		// - Model 에 추가된 속성 중 key 값이 일치하는 속성을 session scope 로 변경
		
		return "redirect:/"; // 메인페이지 재요청
	}
}
