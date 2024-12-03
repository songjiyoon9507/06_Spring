package com.home.jpamember.member.model.dto;

import com.home.jpamember.member.model.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDTO {

	// Fields
	private int num;
	private String name;
	private String id;
	private String phone;
	
	// toEntity() 메서드 통해서 member 생성
	public Member toEntity() {
		return new Member(num, name, id, phone);
	}
}
