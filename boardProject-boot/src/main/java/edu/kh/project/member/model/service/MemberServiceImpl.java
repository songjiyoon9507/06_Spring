package edu.kh.project.member.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;

@Transactional(rollbackFor=Exception.class) // 해당 클래스 메서드 종료 시 까지
               // - 예외(RuntimeException)가 발생하지 않으면 commit
               // - 예외(RuntimeException)가 발생하면 rollback
@Service // 비즈니스 로직 처리 역할 명시 + Bean 등록
@Slf4j
public class MemberServiceImpl implements MemberService {

	// 등록된 Bean 중에서 같은 타입 또는 상속관계인 bean 을 
	// 자동으로 의존성 주입 (DI)
	@Autowired
	private MemberMapper mapper;

	// BCrypt 암호화 객체 의존성 주입(SecurityConfig 참고)
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	// 로그인 서비스
	@Override
	public Member login(Member inputMember) {
		
		// memberEmail : user01@kh.or.kr
		// memberPw : pass01!
		// 암호화 거쳐진 비밀번호를 가지고 와서 평문 상태 암호를 비교
		
		// 테스트
		
		// bcrypt.encode(문자열) : 문자열을 암호화하여 반환
//		String bcryptPassword = bcrypt.encode(inputMember.getMemberPw());
		
//		log.debug("bcryptPassword : " + bcryptPassword);
		// bcryptPassword : $2a$10$h9WqHozrLDEPb2H.WeksFu/84M8bLDi4pDYU9.kTabNp5pdDF9Lae
		
//		boolean result = bcrypt.matches(inputMember.getMemberPw(), bcryptPassword);
//		log.debug("result : " + result);
		
		// 1. 이메일이 일치하면서 탈퇴하지 않은 회원 조회
		Member loginMember = mapper.login(inputMember.getMemberEmail());
		
		// 2. 만약에 일치하는 이메일이 없어서 조회 결과가 null 인 경우
		if(loginMember == null) return null;
		
		// 3. 입력 받은 비밀번호(inputMember.getMemberPw() 평문)와
		//    암호화된 비밀번호(loginMember.getMemberPw())
		//    두 비밀번호가 일치하는지 확인
		
		// 일치하지 않으면
		if(!bcrypt.matches(inputMember.getMemberPw(), loginMember.getMemberPw())) {
			return null;
		}
		
		// 로그인 결과에서 비밀번호 제거
		loginMember.setMemberPw(null);
		
		return loginMember;
	}

	// 이메일 중복 검사
	@Override
	public int checkEmail(String memberEmail) {
		return mapper.checkEmail(memberEmail);
	}

	// 닉네임 중복 검사
	@Override
	public int checkNickname(String memberNickname) {
		log.debug("닉네임 숫자" + mapper.checkNickname(memberNickname));
		return mapper.checkNickname(memberNickname);
	}

	// 회원 가입 서비스
	@Override
	public int signup(Member inputMember, String[] memberAddress) {
		
		// 배열로 넘어온 주소 가공
		// 주소가 입력되지 않으면
		// inputMember.getMemberAddress() -> ",," 구분자만 남아있음
		// memberAddress -> [,,]
		// => 구분자 바꿔서 다시 세팅해줄 거
		
		// 주소가 입력된 경우
		if(!inputMember.getMemberAddress().equals(",,")) {
			
			// String.join("구분자", 배열)
			// -> 배열의 모든 요소 사이에 "구분자"를 추가하여
			// 하나의 문자열로 만들어 반환하는 메서드
			
			// 구분자로 "^^^" 쓴 이유 :
			// -> 주소, 상세주소에 없는 특수문자 작성
			// -> 나중에 다시 3분할 할 때 구분자로 이용할 예정
			String address = String.join("^^^", memberAddress);
			
			// inputMember 주소로 합쳐진 주소를 세팅
			inputMember.setMemberAddress(address);
			
		} else {
			// 주소가 입력되지 않은 경우
			inputMember.setMemberAddress(null); // null 로 저장
		}
		
		// 이메일, 비밀번호(pass02!), 닉네임, 전화번호, 주소
		// 비밀번호 암호화해서 inputMember에 세팅
		String encPw = bcrypt.encode(inputMember.getMemberPw());
		
		inputMember.setMemberPw(encPw);
		
		// 회원 가입 매퍼 메서드 호출
		return mapper.signup(inputMember);
	}

	@Override
	public Member testLogin(String memberEmail) {
		return mapper.testLogin(memberEmail);
	}

	@Override
	public List<Member> memberList() {
		
		return mapper.memberList();
	}

	// 빠른 로그인
	// -> 일반 로그인에서 비밀번호 비교만 제외
	@Override
	public Member quickLogin(String memberEmail) {
		
		Member loginMember = mapper.login(memberEmail);
		
		// 탈퇴 or 없는 회원
		if(loginMember == null) return null;
		
		// 조회된 비밀번호 null 로 변경
		loginMember.setMemberPw(null);
		
		return loginMember;
	}

	@Override
	public List<Member> selectAll() {
		return mapper.selectAll();
	}

	// 비밀번호 초기화
	@Override
	public int resetPw(int memberNo) {
		String encPw = bcrypt.encode("pass01!");
		
//		log.debug("encPw : " + encPw);
		
		Member resetMember = new Member();
		
		resetMember.setMemberNo(memberNo);
		resetMember.setMemberPw(encPw);
		
		return mapper.resetPw(resetMember);
	}

	@Override
	public int restorationMemberNo(int memberNo) {
		
		// 이메일 한번 조회해서 같은 이메일 있는지도 체크
		
		return mapper.restorationMemberNo(memberNo);
	}


	
	
}

/* BCrypt 암호화 (Spring Security 제공)
 * Spring Security 사용하려면 SecurityConfig 필요함 설정용 클래스
 * 
 * - 입력된 문자열(비밀번호)에 salt 를 추가한 후 암호화
 * 
 * ex) A 회원 : 1234     ->     $12!asdfg
 * ex) B 회원 : 1234     ->     $12!dfhfd
 * 
 * 같은 비밀번호를 쳐도 다른 암호가 나옴
 * 
 * - 비밀번호 확인 방법
 * -> BCryptPasswordEncoder.matches(평문 비밀번호, 암호화된 비밀번호)
 * -> 평문 비밀번호와 암호화된 비밀번호가 같은 경우 true 아니면 false 반환
 * 
 * * 로그인 / 비밀번호 변경 / 탈퇴 등 비밀번호가 입력되는 경우
 *   DB에 저장된 암호화된 비밀번호를 조회해서
 *   matches() 메서드로 비교해야함
 * 
 * (참고)
 * BCrypt 이전 sha 방식 암호화 사용 (해킹 위험이 조금 높음)
 * ex) A 회원 : 1234     ->     암호화 : abcd
 * ex) B 회원 : 1234     ->     암호화 : abcd (암호화 시 변경된 내용이 같음)
 * */
