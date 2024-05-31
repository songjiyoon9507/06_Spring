package com.home.board.main.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// 공공데이터 사용시 config.properties 에서 serviceKey 받아와서 써야함
@PropertySource("classpath:/config.properties")
@Controller
public class MainController {

	// @Value 로 값 가져오기
	// decode 값을 가져오는 이유
	// : JS 에서 URLSearchParams 사용 시 문자열을 encoding 해주기 때문에 두번 encoding 되면 문자열이
	//   이상하게 가져와짐, decode 로 가져와서 사용해야함
	// Value 는 SpringFramework 에서 가져와야하고 # 이 아닌 $ 사용
	@Value("${my.public.data.service.key.decode}")
	private String decodeServiceKey;
	
	@RequestMapping("/")
	public String mainPage() {
		return "common/main";
	}
	
	// LoginFilter -> loginError 리다이렉트
	// -> message 만들어서 메인페이지로 리다이렉트
	@GetMapping("loginError")
	public String loginError(RedirectAttributes ra) {
		ra.addFlashAttribute("message", "로그인 후 이용해주세요.");
		return "redirect:/";
	}
	
	/** 공공 데이터 서비스키 반환하기 (비동기 요청 method 작성 X 기본 GetMapping, 값 그대로 돌려줄 거)
	 * @return decodeServiceKey (디코딩된 서비스 키 config.properties 에 있음)
	 */
	@GetMapping("getServiceKey")
	@ResponseBody
	public String getServiceKey() {
		return decodeServiceKey;
	}
	
	@GetMapping("calendar")
	public String calendar() {
		return "/calendar/calendar";
	}
}
