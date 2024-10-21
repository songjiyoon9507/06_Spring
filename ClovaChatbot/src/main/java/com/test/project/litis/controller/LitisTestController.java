package com.test.project.litis.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.test.project.litis.model.dto.Organization;
import com.test.project.litis.model.service.LitisTestService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("litis")
@RequiredArgsConstructor
public class LitisTestController {
	
	private final LitisTestService service;

	@GetMapping("/test")
	public String litisTestMain() {
		return "litis/litisMain";
	}
	
	@GetMapping("/organization")
	public String litisOrganization(Model model) {
		
		List<Organization> list = service.selectAll();
		
		log.info("organization list : " + list);
		
		model.addAttribute("organizationList", list);
		
		return "litis/organization";
	}
}
