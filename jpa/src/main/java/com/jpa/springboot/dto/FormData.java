package com.jpa.springboot.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class FormData {
	private String id;
	private String name;
	private String email;
	private int age;
	// Primitive int 기본값은 0 null 값을 가질 수 없음.
	// null 값을 가지려면 Integer로 선언
	private String gender;
}
