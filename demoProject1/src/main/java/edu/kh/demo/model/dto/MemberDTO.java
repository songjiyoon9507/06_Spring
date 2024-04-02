package edu.kh.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// Lombok : 자주 사용하는 코드를 컴파일 시 자동 완성 해주는 라이브러리
// -> DTO(기본생성자, getter/setter, toString) + Log(slf4j)

@ToString           // toString 오버라이딩 자동완성
@AllArgsConstructor // 매개변수생성자 자동완성
@NoArgsConstructor  // 기본생성자 자동완성
@Getter             // getter 자동완성
@Setter             // setter 자동완성
public class MemberDTO {
	
	private String memberId;
	private String memberPw;
	private String memberName;
	private int memberAge;
	
	// @ModelAttribute name 속성값이랑 필드명이 같으면 가져다 세팅해줌
	// param-main.html 에서 name 속성값으로 적어둔 거랑 똑같이 필드명 적음
}
