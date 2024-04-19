package com.home.board.member.model.service;

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
