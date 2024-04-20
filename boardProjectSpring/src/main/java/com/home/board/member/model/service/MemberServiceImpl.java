package com.home.board.member.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.home.board.member.model.dto.Member;
import com.home.board.member.model.mapper.MemberMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackFor=Exception.class)
@Slf4j
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberMapper mapper;

	// BCrypt 암호화 객체 의존성 주입(SecurityConfig 참고)
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	// 로그인 서비스
	@Override
	public Member login(Member inputMember) {

		// memberEmail
		// memberPw -> DB에 저장할 때 평문 상태로 저장하면 안됨(암호화한 후 저장)
		
		// 테스트
		
		// bcrypt.encode("평문 비밀번호") : 평문비밀번호를 암호화하여 반환
		String bcryptPassword = bcrypt.encode(inputMember.getMemberPw());
		
		log.debug("bcryptPassword : " + bcryptPassword);
		
		boolean result = bcrypt.matches(inputMember.getMemberPw(), bcryptPassword);
		log.debug("result : " + result);
		// result : true
		
		// 1. 이메일이 일치하면서 탈퇴하지 않은 회원 조회
		Member loginMember = mapper.login(inputMember.getMemberEmail());
		
		// 2. 만약에 일치하는 이메일이 없어서 조회 결과가 null 인 경우
		if(loginMember == null) return null;
		
		// loginMember 조회 결과가 있을 때
		// 3. loginMember 에 담긴 암호화된 비밀번호 와 입력받은 평문 비밀번호 일치하는지 비교해줘야함
		
		// 일치하지 않으면 return null;
		if(!bcrypt.matches(inputMember.getMemberPw(), loginMember.getMemberPw())) {
			return null;
		}
		
		// 비밀번호가 일치할 때 로그인 시켜줘야함
		// 단, 로그인 결과에서 가지고 온 비밀번호는 제거시켜줘야함
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
		return mapper.checkNickname(memberNickname);
	}

	// 회원 가입 서비스
	@Override
	public int signup(Member inputMember, String[] memberAddress) {
		
		// 주소는 필수 항목 X
		// 주소가 입력되지 않았을 때
		// inputMember.getMemberAddress() -> ",," 구분자만 남아있음
		// memberAddress -> [,,]
		// -> 주소 안에 , 가 들어가는 경우가 있어서 구분자가 , 이렇게 돼있으면 다루기 힘듦
		
		// 주소가 입력된 경우
		if(!inputMember.getMemberAddress().equals(",,")) {
			
			// Stirng.join("구분자", 배열)
			// -> 배열의 모든 요소 사이에 "구분자"를 추가하여
			// 하나의 문자열로 만들어 반환하는 메서드
			
			// 구분자로 "^^^" 쓴 이유 :
			// -> 주소, 상세주소에 없는 특수문자 작성
			// -> 나중에 다시 3분할할 때 구분자로 이용할 예정
			String address = String.join("^^^", memberAddress);
			
			// inputMember 주소로 합쳐진 주소를 세팅
			inputMember.setMemberAddress(address);
			
		} else {
			// 주소 입력되지 않은 경우
			inputMember.setMemberAddress(null); // null 저장
		}
		
		// 이메일, 비밀번호, 닉네임, 전화번호, 주소
		// 평문형태로 받아온 비밀번호를 암호화해서 DB에 넣어줘야함
		String encPw = bcrypt.encode(inputMember.getMemberPw());
		inputMember.setMemberPw(encPw);
		
		// 회원 가입 mapper 메서드 호출하기
		return mapper.signup(inputMember);
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

	// 회원 목록 조회
	@Override
	public List<Member> selectMemberList() {
		return mapper.selectMemberList();
	}

	// 특정 회원 비밀번호 초기화
	@Override
	public int resetPw(int inputNo) {

		// pass01! -> 암호화
		String encPw = bcrypt.encode("pass01!");
		
		Map<String, Object> map = new HashMap<>();
		map.put("inputNo", inputNo);
		map.put("encPw", encPw);
		
		return mapper.resetPw(map);
	}

	// 특정 회원 탈퇴 복구
	@Override
	public int restorationMemberNo(int memberNo) {
		return mapper.restorationMemberNo(memberNo);
	}
}

/* BCrypt 암호화 (Spring Security 제공)
 * 
 * - 입력된 문자열(비밀번호)에 salt 를 추가한 후 암호화하는 애
 *   (똑같은 평문을 암호화해도 다 다른 결과값이 나옴)
 * 
 * - 비밀번호 확인 방법
 *   -> BCryptPasswordEncoder.matches(평문 비밀번호, 암호화된 비밀번호)
 *      -> 평문 비밀번호와 암호화된 비밀번호가 같은 경우 true 아니면 false 반환
 *      
 * * 로그인 / 비밀번호 변경 / 탈퇴 등 비밀번호가 입력되는 경우
 *   DB에 저장된 암호화된 비밀번호를 조회해서 matches() 메서드로 비교해야 함.
 * */
