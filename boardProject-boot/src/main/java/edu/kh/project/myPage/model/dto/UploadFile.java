package edu.kh.project.myPage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/* @Builder : 빌더 패턴을 이용해서 객체 생성 및 초기화를 쉽게 진행
 * -> Builder 썼으면 기본생성자 꼭 넣어줘야함
 * -> 기본 생성자 자동으로 생성 안됨
 * -> MyBatis 조회 결과를 담을 때 기본생성자로 객체를 만들기 때문 (기본 생성자는 항상 넣는다고 생각하면 됨)
 * 
 * (매개변수생성자 만들어도 기본생성자 자동 생성 안되기 때문에 어차피 써줘야함)
 * Builder 어노테이션은 객체 생성할 때 사용 ServiceImpl
 * */

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UploadFile {

	private int fileNo;
	private String filePath;
	private String fileOriginalName;
	private String fileRename;
	private String fileUploadDate;
	private int memberNo;
	private String memberNickname;
}
