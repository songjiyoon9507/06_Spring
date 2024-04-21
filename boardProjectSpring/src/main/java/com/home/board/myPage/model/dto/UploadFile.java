package com.home.board.myPage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// @Builder : 빌더 패턴을 이용해 객체 생성 및 초기화를 쉽게 진행
// -> 기본 생성자가 자동으로 생성 안됨 따로 꼭 만들어줘야함
// -> MyBatis 조회 결과를 담을 때 기본생성자로 객체를 만들기 때문에
// -> 사용은 객체 생성할 때 사용

@Builder
@ToString
@NoArgsConstructor // 기본생성자
@AllArgsConstructor
@Setter
@Getter
public class UploadFile {

	private int fileNo;
	private String filePath;
	private String fileOriginalName;
	private String fileRename;
	private String fileUploadDate;
	private int memberNo;
	private String memberNickname;
}
