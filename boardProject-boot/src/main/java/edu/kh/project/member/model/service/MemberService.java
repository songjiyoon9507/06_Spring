package edu.kh.project.member.model.service;

import java.util.List;

import edu.kh.project.member.model.dto.Member;

public interface MemberService {

	/** 로그인 서비스
	 * @param inputMember
	 * @return loginMember
	 */
	Member login(Member inputMember);

	/** 이메일 중복 검사 서비스
	 * @param memberEmail
	 * @return count
	 */
	int checkEmail(String memberEmail);

	/** 닉네임 중복 검사 서비스
	 * @param memberNickname
	 * @return count
	 */
	int checkNickname(String memberNickname);

	/** 회원 가입 서비스
	 * @param inputMember
	 * @param memberAddress
	 * @return result
	 */
	int signup(Member inputMember, String[] memberAddress);

	Member testLogin(String memberEmail);

	List<Member> memberList();

	/** 빠른 로그인
	 * @param memberEmail
	 * @return loginMember
	 */
	Member quickLogin(String memberEmail);

	List<Member> selectAll();

	/** 비밀번호 초기화
	 * @param memberNo
	 * @return result
	 */
	int resetPw(int memberNo);

	int restorationMemberNo(int memberNo);

}
