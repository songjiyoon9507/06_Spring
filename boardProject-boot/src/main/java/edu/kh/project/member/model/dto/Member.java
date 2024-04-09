package edu.kh.project.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//DTO(Data Transfer Object)
//- 데이터 전달용 객체
//- DB에 조회된 결과 또는 SQL 구문에 사용할 값을 전달하는 용도
//- 관련성 있는 데이터를 한 번에 묶어서 다룸

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Member {
	// @Builder 객체 만들 때 쉽게 해주는 어노테이션
	
	private int memberNo;
	private String memberEmail;
	private String memberPw;
	private String memberNickname;
	private String memberTel;
	private String memberAddress;
	private String profileImg;
	private String enrollDate;
	private String memberDelFl;
	private int authority; // 관리자인지 일반 멤버인지 1 일반회원 2 관리자
}
