package com.home.board.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.home.board.member.model.dto.Member;
import com.home.board.member.model.service.MemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
			Model model,
			@RequestParam(value="saveId", required = false) String saveId,
			HttpServletResponse resp) {
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
			
			// @SessionAttributes ({"key", "key", ...})
			// - Model 에 추가된 속성 중 key 값이 일치하는 속성을 session scope 로 변경
			
			// -------------------------------------------------------------------------
			
			// 아이디 저장(Cookie)
			
			// 쿠키 객체 생성 (K:V) import jakarta.servlet.http.Cookie;
			Cookie cookie = new Cookie("saveId", loginMember.getMemberEmail());
			// saveId=user01@kh.or.kr
			
			// 클라이언트가 어떤 요청을 할 때 쿠키가 첨부될지 지정해줄 거
			
			// .setPath() 쿠키를 담아줄 경로 지정
			cookie.setPath("/");
			// "/" 는 최상위 주소 IP 또는 도메인 또는 localhost 뒤에 "/" 이게 붙는다는 뜻
			// --> 메인 페이지 + 그 하위 모든 주소

			// 만료 기간 지정 (조건을 따져줘야함)
			// 아이디 저장 체크 박스에 체크가 된 경우와 체크가 안된 경우
			
			// 체크박스
			// - 체크가 된 경우 : "on"
			// - 체크가 안된 경우 : null
			
			if(saveId != null) { // 아이디 저장 체크 시
				cookie.setMaxAge(60 * 60 * 24 * 30); // 30일 (초 단위로 지정)
			} else { // 체크 안 했을 때
				// 기존에 아이디 저장을 사용하다가 체크를 해제한 경우
				cookie.setMaxAge(0); // 0초 (클라이언트 쿠키 삭제)
			}
			
			// 서버에서 쿠키 만들어서 넣어줌 쿠키는 브라우저가 관리하는 거
			// 브라우저로 보내줘야함
			// 응답객체 resp
			// 응답 객체에 쿠키 추가해서 클라이언트로 전달할 거
			resp.addCookie(cookie);			
			
		}
		
		return "redirect:/"; // 메인페이지 재요청
	}
	
	/** 로그아웃 : Session 에 저장된 로그인된 회원 정보를 없앰(만료,무효화)
	 * 
	 * @param SessionStatus : 세션을 완료(없앰) 시키는 역할의 객체
	 * 						  현재 세션의 상태를 다룰 수 있는 거
	 * 						- @SessionAttributes 로 등록된 세션을 만료
	 * 						- 서버에서 기존 세션 객체가 사라짐과 동시에
	 * 						  새로운 세션 객체가 생성되어 클라이언트와 연결
	 * 						  (기존 정보들은 다 파기됨)
	 * ==> 파기된 후에 다시 생성될 때 sessionId 도 다시 발급됨
	 * 
	 * @return redirect:/
	 */
	@GetMapping("logout")
	public String logout(SessionStatus status) {
		
		status.setComplete(); // 세션을 완료 시킴(없앰)
		
		// 메인페이지 리다이렉트
		return "redirect:/";
	}
	
	/** 회원 가입 페이지로 이동
	 * @return member/signup
	 */
	@GetMapping("signup")
	public String signupPage() {
		return "member/signup";
	}
	
	@ResponseBody // 응답 본문(요청한 fetch)으로 돌려보냄
	@GetMapping("checkEmail")
	public int checkEmail(@RequestParam("memberEmail") String memberEmail) {
		return service.checkEmail(memberEmail);
	}
}
