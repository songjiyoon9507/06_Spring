package edu.kh.project.myPage.model.service;

import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.mapper.MyPageMapper;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor=Exception.class) // 모든 예외 발생 시 롤백 rollbackFor 안 쓰면 runtimeException 까지만
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

	private final MyPageMapper mapper;

	// BCrypt 암호화 객체 의존성 주입(SecurityConfig 참고)
	private final BCryptPasswordEncoder bcrypt;
	
	// 회원 정보 수정
	@Override
	public int updateInfo(Member inputMember, String[] memberAddress) {
		
		// memberAddress 가공해줘야함
		// 입력된 주소가 있을 경우
		// memberAddress 를 우편주소^^^도로명지번/주소^^^상세주소 형태로 가공
		
		// 주소 입력 안했을 경우 null 대입
		// 주소 입력 X -> inputMember.getMemberAddress() -> ",,"
//		if(inputMember.getMemberAddress().equals(",,")) {
//			
//			// 주소에 null 대입
//			inputMember.setMemberAddress(null);
//		} else {
//			// memberAddress 를 가공
//			String address = String.join("^^^", memberAddress);
//			
//			// 주소에 가공된 데이터를 대입
//			inputMember.setMemberAddress(address);
//		}
		
		
		if(!inputMember.getMemberAddress().equals(",,")) { // 주소 입력 됐을 때
			String address = String.join("^^^", memberAddress);
			
			inputMember.setMemberAddress(address);
		} else {
			// 주소 입력 되지 않은 경우
			inputMember.setMemberAddress(null);
		}
		
		// SQL 수행 후 결과 반환
		return mapper.updateInfo(inputMember);
	}

	// 비밀번호 수정
	@Override
	public int changePw(Map<String, Object> paramMap, int memberNo) {
		
		// 현재 로그인한 회원의 암호화된 비밀번호를 DB에서 조회
		String originPw = mapper.selectPw(memberNo);
		
		// 암호화된 비밀번호를 평문으로 바꿔서 비교해줘야함
		// 입력받은 현재 비밀번호와 (평문)
		// DB에서 조회한 비밀번호 비교(암호화)
		// BCryptPasswordEncoder.matches(평문, 암호화된 비밀번호)
		
		// 다를 경우
		if(!bcrypt.matches((String)paramMap.get("currentPw"), originPw)) {
			return 0;
		}
		
		// 같을 경우
		
		// 새 비밀번호를 암호화해서 업데이트
		// 새 비밀번호 암호화 진행
		String encPw = bcrypt.encode((String)paramMap.get("newPw"));
		paramMap.put("encPw", encPw);
		paramMap.put("memberNo", memberNo);
		
		return mapper.changePw(paramMap);
	}

	// 회원 탈퇴
	@Override
	public int secession(String memberPw, int memberNo) {
		
		String originPw = mapper.selectPw(memberNo);
		
		if(!bcrypt.matches(memberPw, originPw)) {
			return 0;
		}
		
		return mapper.secession(memberNo);
	}
}
