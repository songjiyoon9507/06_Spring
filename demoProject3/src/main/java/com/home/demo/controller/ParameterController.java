package com.home.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

// Bean : 스프링이 만들고 관리하는 객체

@Controller // 요청 / 응답 제어 역할 명시 + Bean 등록
@RequestMapping("param") // 공통주소 param /param 으로 시작하는 모든 요청이 현재 컨트롤러로 매핑
@Slf4j // log 사용할 거라고 알려주는 거 lombok 에서 지원 log 를 이용한 메세지 출력 시 사용 (Lombok 라이브러리에서 제공)
public class ParameterController {

	@GetMapping("main") // /param/main GET 방식 요청 매핑
	public String paramMain() {
		
		// classpath: src/main/resources
		// 접두사 : classpath:/templates/
		// 접미사 : .html
		// -> src/main/resources/templates/param/param-main.html
		return "param/param-main";
	}
	
	/* 1. HttpServletRequest.getParameter("key") 이용
	 * 
	 * HttpServletRequest :
	 * - 요청 클라이언트 정보, 제출된 파라미터 등을 저장한 객체
	 * - 클라이언트 요청 시 생성
	 * 
	 * 전달인자 해결사  전달인자 Argument 해결사 Resolver
	 * 
	 * ArgumentResolver (전달 인자 해결사)
	 * Spring Boot 가 매개변수 전달인자 보고 알아서 불러줌
	 * - Spring 의 Controller 메서드 작성 시
	 * 매개변수에 원하는 객체를 작성하면
	 * 존재하는 객체를 바인딩 또는 없으면 생성해서 바인딩
	 * */
	
	@PostMapping("test1") // /param/test1 POST 방식 요청 매핑
	public String paramTest1(HttpServletRequest req) {
		
		String inputName = req.getParameter("inputName");
		int inputAge = Integer.parseInt(req.getParameter("inputAge"));
		String inputAddress = req.getParameter("inputAddress");
		
		// debug : 코드 오류 해결
		// -> 코드 오류 없는데 정상 수행이 안될 때
		// -> 값이 잘못된 경우 -> 값 추적
		// debug 내역을 log 에 띄워줄 거임
		// log 사용하려면 Controller 위에 @Slf4j 로그 추가해줘야함 (Lombok 에서 지원)
		log.debug("inputName : " + inputName);
		log.debug("inputAge : " + inputAge);
		log.debug("inputAddress : " + inputAddress);
		
		/* log 확인하기
		 * 나중에 log 내보내는 파일 만들어서 저장해놓고 추적할 수 있음
		 * 
		 * inputName : 홍길동
		 * inputAge : 20
		 * inputAddress : 콘솔 창 확인하기
		 * */
		
		// redirect
		/* Spring 에서 Redirect(재요청) 하는 방법
		 * 
		 * -Controller 메서드 반환 값에
		 * "redirect:요청주소"; 작성
		 * */
		return "redirect:/param/main";
	}
	
	/* 2. @RequestParam 어노테이션을 이용 - 낱개 파라미터 얻어오기 (쓰고 싶은만큼 써서 가져오면 됨)
	 * 
	 * - request 객체를 이용한 파라미터 전달 어노테이션
	 * - 매개변수 앞에 해당 어노테이션을 작성하면, 매개변수에 값이 주입됨.
	 * - 주입되는 데이터는 매개변수의 타입에 맞게 형변환/파싱이 자동으로 수행됨
	 * 
	 * [기본 작성법]
	 * @RequestParam("key") 자료형 매개변수명
	 * key = name값
	 * */
	@PostMapping("test2")
	public String paramTest2(@RequestParam("title") String title,
			@RequestParam("writer") String writer,
			@RequestParam("price") int price,  // 자동으로 형변환 됨 강제 형변환 안해줘도 됨
			@RequestParam(value="publisher", required=false) String publisher
			) {
		
		log.debug("title : " + title);
		log.debug("writer : " + writer);
		log.debug("price : " + price);
		log.debug("publisher : " + publisher);
		/* log 확인하기
		 * 
		 * title : 어린왕자
		 * writer : 생택쥐베리
		 * price : 11000
		 * */
		
		/* @RequestParam("publisher") String publisher 구문 추가 html 에서는 input 받는 창 없음
		 * => 404 bad request 에러남
		 * 
		 * @RequestParam
		 * 기본 작성법대로 작성하면 요청 받을 때 parameter 값이 꼭 있어야함
		 * 
		 * [속성 추가 작성법]
		 * @RequestParam(value="name", required="false", defaultValue="1")
		 * 
		 * value : 전달받은 input 태그의 name 속성값
		 * 
		 * required : 입력된 name 속성값의 parameter 필수 여부 지정(기본값이 true)
		 * -> false 로 되어있으면 들어온 값이 없어도 에러 안 남
		 * publisher : null
		 * (required=false 작성하고 defaultValue 작성 안하고, input 작성 안했을 때)
		 * 
		 * defaultValue : 아무것도 입력 안했을 때 default 로 지정해놓은 값 들어감
		 * 파라미터 중 일치하는 name 속성값이 없을 경우에 대입할 값 지정
		 * -> required=false 인 경우 사용
		 * defaultValue 적었을 때 publisher : ABC출판사
		 * */
		
		return "redirect:/param/main";
	}
}
