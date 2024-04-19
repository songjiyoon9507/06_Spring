package com.home.board.email.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.home.board.email.model.service.EmailService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("email")
@RequiredArgsConstructor
public class EmailController {

	private final EmailService service;
	
	/** 이메일 보내기
	 * @param email
	 * @return
	 */
	@ResponseBody
	@PostMapping("signup")
	public int signup(@RequestBody String email) {
		
		String authKey = service.sendEmail("signup", email);
		
		if(authKey != null) { // 인증번호가 반환돼서 돌아옴
			// 이메일 보내기 성공
			return 1;
		}
		
		// 이메일 보내기 실패
		return 0;
	}
	
	@ResponseBody
	@PostMapping("checkAuthKey")
	public int checkAuthKey(@RequestBody Map<String, Object> map) {
		
		// 입력 받은 이메일, 인증번호가 DB에 있는지 조회
		// 이메일 있고, 인증번호 일치 == 1
		// 아니면 0
		return service.checkAuthKey(map);
	}
}
