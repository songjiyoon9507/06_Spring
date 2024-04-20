package com.home.board.myPage.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.home.board.member.model.dto.Member;

@Mapper
public interface MyPageMapper {

	/** 회원 정보 수정
	 * @param inputMember
	 * @return result
	 */
	int updateInfo(Member inputMember);

}
