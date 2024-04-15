package edu.kh.project.myPage.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.service.MyPageService;
import lombok.RequiredArgsConstructor;

@SessionAttributes({"loginMember"})
@Controller
@RequestMapping("myPage")
@RequiredArgsConstructor
public class MyPageController {

	private final MyPageService service;
	
	// 닉네임 클릭했을 때 화면 마이페이지로 화면 전환
	// /myPage/info (GET)
	/** 내 정보 조회/수정 화면으로 전환
	 * @param loginMember : 세션에 존재하는 loginMember를 얻어와 매개변수에 대입
	 * @param model : 데이터 전달용 객체 (기본 request scope)
	 * @return myPage/myPage-info로 요청 위임
	 */
	@GetMapping("info")
	public String info(
				@SessionAttribute("loginMember") Member loginMember,
				Model model
			) {
		
		/* Session scope 에서 loginMember 꺼내 올 때
		 * 매개변수 자리에 @SessionAttribute("loginMember") Member loginMember
		 * 작성하고 Controller 클래스 위에 @SessionAttributes({"loginMember"}) 어노테이션 작성
		 * */
		
		// 세션에 loginMember 정보 올려둠
		// 주소 구분자 ^^^ 로 돼있음 가공해줄 거
		// 주소만 꺼내옴
		String memberAddress = loginMember.getMemberAddress();
		
		// 주소가 있을 경우에만 동작
		if(memberAddress != null) {
			
			// 구분자 "^^^"를 기준으로
			// memberAddress 값을 쪼개어 String[]로 반환
			String[] arr = memberAddress.split("\\^\\^\\^");
			// regex 정규표현식 전달해야하는 문자열이 일반 문자열이 아님 정규표현식
			// -> 정규표현식에서는 \\ 이스케이프 문자 \ 한개만 쓰면 안됨
			
			// "04540^^^서울 중구 남대문로 120^^^3층 E강의실"
			// --> ["04540", "서울 중구 남대문로 120", "3층 E강의실"]
			model.addAttribute("postcode", arr[0]);
			model.addAttribute("address", arr[1]);
			model.addAttribute("detailAddress", arr[2]);
			
		}
		
		// /templates/myPage/myPage-info.html 로 forward
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
	 * @param ra : 리다이렉트 시 request scope 로 데이터 전달
	 * @return redirect:info
	 */
	@PostMapping("info")
	public String updateInfo(Member inputMember,
				@SessionAttribute("loginMember") Member loginMember,
				@RequestParam("memberAddress") String[] memberAddress,
				RedirectAttributes ra
				) {
		
		// inputMember service 에 보내줄 거임
		// inputMember 에 로그인한 회원번호 추가
		inputMember.setMemberNo(loginMember.getMemberNo());
		
//		int memberNo = loginMember.getMemberNo();
//		inputMember.setMemberNo(memberNo);
		
		// 회원 정보 수정 서비스 호출
		int result = service.updateInfo(inputMember, memberAddress);
		
//		String message = null;
//		
//		if(result > 0) { // 수정 성공했을 때
//			message = "회원 정보 수정 성공";
//		} else {
//			message = "회원 정보 수정 실패";
//		}
//		ra.addFlashAttribute("message", message);
		
		if(result > 0) { // 수정 성공했을 때
			ra.addFlashAttribute("message", "회원 정보 수정 성공");
			
			// loginMember는
			// 세션에 저장된 로그인한 회원 정보가 저장된 객체를 참조하고 있다.
			// session 에 올라가져 있는 loginMember 수정 데이터 업데이트 해줘야함
			
			// -> loginMember를 수정하면
			//	  세션에 저장된 로그인한 회원 정보가 수정된다
			
			// == 세션 데이터와 DB 데이터를 맞춤
			loginMember.setMemberNickname(inputMember.getMemberNickname());
			loginMember.setMemberTel(inputMember.getMemberTel());
			loginMember.setMemberAddress(inputMember.getMemberAddress());
			
		} else {
			ra.addAttribute("message", "회원 정보 수정 실패");
		}
		return "redirect:info";
	}
	
	/** 비밀번호 변경
	 * @param paramMap : 모든 파라미터를 맵으로 저장
	 * @param loginMember : 세션에 등록된 로그인한 회원 정보
	 * @param ra
	 * @return 
	 */
	@PostMapping("changePw")
	public String changePw(
				@RequestParam Map<String, Object> paramMap,
				@SessionAttribute("loginMember") Member loginMember,
				RedirectAttributes ra
			) {
		
		// 로그인한 회원 번호 얻어오기
		int memberNo = loginMember.getMemberNo();
		
		// 현재 + 새 + 회원번호를 서비스로 전달
		int result = service.changePw(paramMap, memberNo);
		
		// 분기처리
		String path = null;
		String message = null;
		
		if(result > 0) {
			path = "/myPage/info";
			message = "비밀번호가 변경 되었습니다.";
		} else {
			path = "/myPage/changePw";
			message = "현재 비밀번호가 일치하지 않습니다.";
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:" + path;
		
	}
	
	/** 회원 탈퇴
	 * @param memberPw : 입력 받은 비밀번호
	 * @param loginMember : 로그인한 회원 정보(세션)
	 * @param status : 세션 완료 용도의 객체
	 * 			-> @SessionAttributes 로 등록된 세션을 완료시킬 거
	 * @param ra
	 * @return
	 */
	@PostMapping("secession")
	public String secession(
			@RequestParam("memberPw") String memberPw,
			@SessionAttribute("loginMember") Member loginMember,
			SessionStatus status,
			RedirectAttributes ra
			) {
		
		// session scope 에 있는 loginMember 를 지워줘야함
		// SessionStatus 이용 등록된 세션 완료
		
		// 서비스 호출
		int memberNo = loginMember.getMemberNo();
		
		int result = service.secession(memberPw, memberNo);

		String message = null;
		String path = null;
		
		if(result > 0) {
			// 탈퇴 성공시
			message = "탈퇴 되었습니다.";
			path = "/";
			
			// loginMember 세션 만료
			status.setComplete(); // 세션 완료 시킴
			
		} else {
			// 탈퇴 실패시
			message = "비밀번호가 일치하지 않습니다.";
			path = "secession";
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:" + path;
	}
}
