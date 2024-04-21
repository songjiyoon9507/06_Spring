package com.home.board.myPage.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.home.board.member.model.dto.Member;
import com.home.board.myPage.model.dto.UploadFile;

@Mapper
public interface MyPageMapper {

	/** 회원 정보 수정
	 * @param inputMember
	 * @return result
	 */
	int updateInfo(Member inputMember);

	/** 암호화된 비밀번호 조회
	 * @param memberNo
	 * @return
	 */
	String selectPw(int memberNo);

	/** 비밀번호 변경
	 * @param paramMap
	 * @return result
	 */
	int changePw(Map<String, Object> paramMap);

	/** 회원 탈퇴
	 * @param memberNo
	 * @return result
	 */
	int secession(int memberNo);

	/** 파일 정보를 DB에 삽입
	 * @param uf
	 * @return result
	 */
	int insertUploadFile(UploadFile uf);

	/** 파일 목록 조회
	 * @return list
	 */
	List<UploadFile> fileList();

	/** 프로필 이미지 변경
	 * @param mem
	 * @return result
	 */
	int profile(Member mem);

}
