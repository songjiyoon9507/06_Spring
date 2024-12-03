package com.home.jpamember.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.home.jpamember.member.model.dto.MemberDTO;
import com.home.jpamember.member.model.entity.Member;
import com.home.jpamember.member.model.repository.MemberRepository;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("jpa")
@Slf4j
public class JpaController {
	
	// MemberRepository 주입
	@Autowired
	private MemberRepository memberRepository;

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
			// 사용자 등록한 값 제대로 들어오는지 확인
			log.info("inputName=={}", memberDTO.getName());
			log.info("inputName=={}", memberDTO.getId());
			log.info("inputName=={}", memberDTO.getPhone());
			log.info("inputName=={}", memberDTO.toString());
			
			// 전달 받은 데이터 DTO 를 Entity 로 변환
			// 1. DTO 클래스 통해서 Entity 객체 생성
			Member member = memberDTO.toEntity();
			
			log.info("member=={}", member.toString());
			
			// 2. Repository 로 Entity 를 DB 에 저장
			// Repository : Spring Data JPA에서 제공, JPA를 사용하여 데이터베이스를 조작하기 위한 메서드들을 제공
			// Repository <Type, ID> Type은 Member (Entity Class)
			// ID는 Entity class의 프라이머리키 아이디 값의 Type 보통은 DB num idx
			// -> Long 이나 Integer
			// member 테이블의 ID는 (Primary key == num) Long 이나 Integer 로 적어주면 됨
			// CRUDRepository, JPARepository, Pasing and SortRepository(페이징 관련 repository) 등등이 있음
			// JPARepository 사용할 것임 (이미 만들어진 것 상속 받아서 사용)
			// MemberRepository Interface 만들어서 JPARepository 상속 받을 것 (따로 구현해줄 필요 없음)
			memberRepository.save(member);
			// DTO 객체를 Entity 로 변환한 member 를 save 메서드로 DB에 저장
			
		} catch (Exception e) {
			// err
			e.printStackTrace();
		}
		
		// 다시 main 으로
		return "redirect:/";
	}
	
}
