package com.home.jpamember.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.home.jpamember.member.model.dto.MemberDTO;

@Controller
@RequestMapping("jpa")
public class JpaController {

	/** 회원 등록 페이지
	 * @param model
	 * @return 회원 등록 페이지
	 */
	@GetMapping("memberWriteForm")
	public String memberWriteForm(Model model) {
		
		// 등록 처리 (신규회원)
		
		return "jpa/memberWriteForm";
	}
	
	/** 회원 등록(클라이언트에서 보낸 값을 받아서 처리)
	 * @return
	 */
	@PostMapping("memberWriteForm")
	public String insertMember(Model model,
			MemberDTO memberDTO) {
		
		try {
			// 등록 처리
		} catch (Exception e) {
			// err
			e.printStackTrace();
		}
		
		return "";
	}
	
}
