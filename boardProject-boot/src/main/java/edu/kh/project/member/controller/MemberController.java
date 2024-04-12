package edu.kh.project.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/* @SessionAttributes( {"key", "key", "key", ...} )
 * - Model 에 추가된 속성 중
 *   key 값이 일치하는 속성을 session scope 로 변경
 * */

@SessionAttributes({"loginMember"}) // loginMember를 session scope 에 세팅하겠다.
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
	 * - 로그인한 정보를 session 에 기록하여
	 *   로그아웃 또는 브라우저 종료 시까지
	 *   해당 정보를 계속 이용할 수 있게 함
	 * */
	
	/** 로그인
	 * @param inputMember : 커맨드 객체 (@ModelAttribute 생략)
	 * 						(memberEmail, memberPw 세팅된 상태)
	 * @param ra : 리다이렉트 시 request scope 로 데이터를 전달하는 객체
	 * @param model : 데이터 전달용 객체 (기본 request scope)
	 * @return "redirect:/"
	 */
	@PostMapping("login")
	public String login(Member inputMember,
				RedirectAttributes ra,
				Model model,
				@RequestParam(value="saveId", required = false) String saveId,
				HttpServletResponse resp) {
		
		// 체크박스
		// - 체크가 된 경우 : "on"
		// - 체크가 안된 경우 : null
		
		// 로그인 서비스 호출
		Member loginMember = service.login(inputMember);
		
		// 로그인 실패 시
		if(loginMember == null) {
			ra.addFlashAttribute("message", "아이디 또는 비밀번호가 일치하지 않습니다.");
		}
		
		// 로그인 성공 시
		if(loginMember != null) {
			// Session scope 에 loginMember 올려둘 거
			// model 은 기본 request scope
			model.addAttribute("loginMember", loginMember);
			// 1단계 : request scope 에 세팅됨
			
			// 2단계 : 클래스 위에 @SessionAttributes({"loginMember"}) 어노테이션 작성
			//         -> session scope 로 이동됨
			
			// ************************************************
			
			// 아이디 저장(Cookie)
			
			// 쿠키 객체 생성 (K:V) jakarta servlet http import
			Cookie cookie = new Cookie("saveId", loginMember.getMemberEmail());
			// saveId=user01@kh.or.kr
			
			// 클라이언트가 어떤 요청을 할 때 쿠키가 첨부될지 지정
			
			// ex) "/" : IP 또는 도메인 또는 localhost
			//           뒤에 "/"  --> 메인 페이지 + 그 하위 주소 모두
			cookie.setPath("/");
			
			// 만료 기간 지정
			if(saveId != null) { // "on" 아이디 저장 체크 시
				cookie.setMaxAge(60 * 60 * 24 * 30); // 30일 초 단위로 지정 만료기간 설정하는 거
			} else { // 미체크 시
				cookie.setMaxAge(0); // 0초 (클라이언트 쿠키 삭제)
			}
			
			// 응답 객체에 쿠키 추가 -> 클라이언트로 전달
			resp.addCookie(cookie);
			
		}
		
		return "redirect:/"; // 메인페이지 재요청
	}
	
	/** 로그아웃 : Session 에 저장된 로그인된 회원 정보를 없앰(만료, 무효화)
	 * 
	 * @param SessionStatus : 세션을 완료(없앰) 시키는 역할의 객체
	 * 		- @SessionAttributes 로 등록된 세션을 만료
	 * 		- 서버에서 기존 세션 객체가 사라짐과 동시에
	 * 		  새로운 세션 객체가 생성되어 클라이언트와 연결 (세션이 비워진 상태)
	 * 
	 * @return "redirect:/"
	 */
	@GetMapping("logout")
	public String logout(SessionStatus status) {
		
		status.setComplete(); // 세션을 완료 시킴(없앰)
		return "redirect:/"; // 메인페이지 리다이렉트
	}
	
	/** 회원 가입 페이지 이동
	 * @return "member/signup"
	 */
	@GetMapping("signup")
	public String signup() {
		
		return "member/signup";
	}
	
	@ResponseBody // 응답 본문(요청한 fetch)으로 돌려보냄
	@GetMapping("checkEmail")
	public int checkEmail(@RequestParam("memberEmail") String memberEmail) {
		
		return service.checkEmail(memberEmail);
	}
	
	/** 닉네임 중복 검사
	 * @param memberNickname
	 * @return 중복 1, 아님 0
	 */
	@ResponseBody
	@GetMapping("checkNickname")
	public int checkNickname(@RequestParam("memberNickname") String memberNickname) {
		
		return service.checkNickname(memberNickname);
	}
	
	/** 회원 가입
	 * @param inputMember : 입력된 회원정보 (memberEmail, memberPw, memberNickname,
	 * 						memberTel, memberAddress(따로 받아서 처리))
	 * @param memberAddress : 입력한 주소 input 3개의 값을 배열로 전달 [우편번호, 도로명/지번주소, 상세주소]
	 * @param ra : 리다이렉트 시 request scope 로 데이터 전달하는 객체
	 * @return
	 */
	@PostMapping("signup")
	public String signup(Member inputMember,
			@RequestParam("memberAddress") String[] memberAddress,
			RedirectAttributes ra) {
		
		// 회원 가입 서비스 호출
		int result = service.signup(inputMember, memberAddress);
		
		String path = null;
		String message = null;
		
		if(result > 0) { // 성공 시
			message = inputMember.getMemberNickname() + "님의 가입을 환영합니다.";
			path = "/";
		} else { // 실패
			message = "회원 가입 실패";
			path = "signup";
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:" + path;
	}
}
