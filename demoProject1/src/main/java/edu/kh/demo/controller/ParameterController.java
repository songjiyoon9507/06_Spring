package edu.kh.demo.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
/*import org.springframework.web.bind.annotation.ModelAttribute;*/
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.kh.demo.model.dto.MemberDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

// Bean : 스프링이 만들고 관리하는 객체
@Controller // 요청/응답 제어 역할 명시 + Bean 등록
@RequestMapping("param") // /param 으로 시작하는 모든 요청을 현재 컨트롤러로 매핑
@Slf4j // log를 이용한 메세지 출력 시 사용 (Lombok 라이브러리에서 제공)
public class ParameterController {

	// @RequestMapping("main")
	@GetMapping("main") // /param/main GET 방식 요청 매핑
	public String paramMain() {
		
		// classpath: src/main/resources
		// 접두사 : classpath:/templates
		// 접미사 : .html
		return "param/param-main";
		// -> src/main/resources/templates/param/param-main.html
	}
	
	/* 1. HttpServletRequest.getParameter("key") 이용
	 * 매개 변수 자리에 넣어줌 HttpServletRequest req
	 * -> 이렇게 넣으면 사용할 수 있음
	 * 
	 * HttpServletRequest : 
	 * - 요청 클라이언트 정보, 제출된 파라미터 등을 저장한 객체
	 * - 클라이언트 요청 시 생성
	 * 
	 * ** 전달 인자 해결사 **
	 * ArgumentResolver(전달 인자 해결사) (Spring 내장 객체)
	 * 생성해서 주입시켜줌(Spring 이)
	 * - Spring 의 Controller 메서드 작성 시
	 * 매개변수에 원하는 객체를 작성하면
	 * 존재하는 객체를 바인딩 또는 없으면 생성해서 바인딩
	 * */
	
	@PostMapping("test1") // /param/test1 POST 방식 요청 매핑
	public String paramTest1(HttpServletRequest req) {
		// html 에서 넘겨준 값 받기
		String inputName = req.getParameter("inputName");
		int inputAge = Integer.parseInt(req.getParameter("inputAge"));
		// int 형으로 형 변환
		String inputAddress = req.getParameter("inputAddress");
		
//		System.out.println(inputName);
		// 얻어온 값 이렇게 확인했었는데
		// 디버깅 console 창에 log 띄워줄거임
		
		// debug : 코드 오류 해결
		// -> 코드 오류 없는데 정상 수행이 안될 때
		// (값이 제대로 넘어오는지 확인해봐야함)
		// => 값이 어떻게 생겼는지 값 추적을 해야함
		// log 사용하겠다고 클래스 단에 알려줘야함
		
		log.debug("inputName : " + inputName);
		log.debug("inputAge : " + inputAge);
		log.debug("inputAddress : " + inputAddress);
		
		// 하나 더 설정해줘야함
		// application.properties 에 설정 logging.level.edu.kh.demo=debug
		
		/* Console 창 확인
[2m2024-04-01T15:43:59.287+09:00[0;39m [32mDEBUG[0;39m [35m19940[0;39m [2m---[0;39m [2m[demoProject1] [p-nio-80-exec-4][0;39m [2m[0;39m[36me.k.demo.controller.ParameterController [0;39m [2m:[0;39m inputName : 홍길동
[2m2024-04-01T15:43:59.287+09:00[0;39m [32mDEBUG[0;39m [35m19940[0;39m [2m---[0;39m [2m[demoProject1] [p-nio-80-exec-4][0;39m [2m[0;39m[36me.k.demo.controller.ParameterController [0;39m [2m:[0;39m inputAge : 20
[2m2024-04-01T15:43:59.287+09:00[0;39m [32mDEBUG[0;39m [35m19940[0;39m [2m---[0;39m [2m[demoProject1] [p-nio-80-exec-4][0;39m [2m[0;39m[36me.k.demo.controller.ParameterController [0;39m [2m:[0;39m inputAddress : 경기도 평택시
		 * */
		
		// forward 경로 써주는 거
		
		/* Spring 에서 Redirect(재요청) 하는 방법
		 * 
		 * - Controller 메서드 반환 값에
		 * "redirect:요청주소"; 작성
		 * */
		return "redirect:/param/main";
		// 공통주소인 param main 나머지 주소 매핑해주는 곳으로 감
	}
	
	/* 2. RequestParam 어노테이션을 이용 - 낱개 파라미터 얻어오기
	 * 
	 * - request 객체를 이용한 파라미터 전달 어노테이션
	 * - 매개변수 앞에 해당 어노테이션을 작성하면, 매개변수에 값이 주입됨
	 * - 주입되는 데이터는 매개변수의 타입에 맞게 형변환/파싱이 자동으로 수행됨
	 * (Integer.ParseInt 이런 거 안해도 됨)
	 * 
	 * [기본 작성법]
	 * @RequestParam("key") 자료형 매개변수명
	 * 
	 * [속성 추가 작성법]
	 * @RequestParam(value="name", required="false", defaultValue="1")
	 * 
	 * value : 전달받은 input 태그의 name 속성값
	 * 
	 * required : 입력된 name 속성값 파라미터 필수 여부 지정(기본값이 true)
	 * -> required = true 인 파라미터가 존재하지 않는다면 400 Bad Request 에러 발생
	 * 
	 * defaultValue : 파라미터 중 일치하는 name 속성값이 없을 경우에 대입할 값 지정.
	 * -> required=false 인 경우 사용
	 * */
	@PostMapping("test2")
	public String paramTest2(@RequestParam("title") String title,
			@RequestParam("writer") String writer,
			@RequestParam("price") int price,
			@RequestParam(value="publisher", required=false, defaultValue="열린책들") String publisher) {
		/*
title : 어린왕자
writer : 생택쥐베리
price : 10100
publisher : 열린책들

default 안 쓰면 publisher : null 이렇게 뜸

publisher 에 값 작성하면 작성한 값 넘어옴 publisher : 느낌표출판사
		 */
		
		// 어노테이션 생략 안됨 에러남
		
		// 그냥 int로 가져올 수 있음
		log.debug("title : " + title);
		log.debug("writer : " + writer);
		log.debug("price : " + price);
		log.debug("publisher : " + publisher);
		// There was an unexpected error (type=Bad Request, status=400).
		// HTML 에서 넘기는 param 값 없을 때 이렇게 뜸
		/*
title : 어린왕자
writer : 생택쥐베리
price : 10000
		 */
		return "redirect:/param/main";
	}
	
	/* 3. @RequestParam 여러 개 파라미터
	 * 
	 * String[]
	 * List<자료형>
	 * Map<String, Object>
	 * 
	 * required 속성은 사용 가능하나,
	 * defaultValue 속성은 사용 불가
	 * */
	
	@PostMapping("test3")
	public String paramTest3(@RequestParam(value="color", required=false) String[] colorArr,
			@RequestParam(value="fruit", required=false) List<String> fruitList,
			@RequestParam Map<String, Object> paramMap
			) {
		// Map 사용할 때는 소괄호 없이 사용할 수 있음
		
		log.debug("colorArr : " + Arrays.toString(colorArr));
		log.debug("fruitList : " + fruitList);
		log.debug("paramMap : " + paramMap);
		
		/*
colorArr : [Red, Green, Blue]
fruitList : [Apple, Banana, Orange]
paramMap : {color=Red, fruit=Apple, productName=책, expirationDate=2024-04-02}

Map 형태로 받을 때는 키가 중복되면 앞에 하나만 나옴(배열형태 아님)
겹치지 않는 name 값들을 가지고 올 때 쓰기 좋음
		 */
		
		// @RequestParam Map<String, Object>
		// -> 제출된 모든 파라미터가 Map 에 저장됨
		// -> 단, key(name 속성값)이 중복되면 처음 들어온 값 하나만 저장된다.
		// -> 같은 name 속성값 파라미터 String[], List 로 저장 X
		
		return "redirect:/param/main";
	}
	
	/* 4. ModelAttribute 를 이용한 파라미터 얻어오기
	 * 
	 * @ModelAttribute
	 * - DTO(또는 VO)와 같이 사용하는 어노테이션
	 * 
	 * 전달 받은 파라미터의 name 속성 값이
	 * 같이 사용되는 DTO의 필드명과 같으면
	 * 자동으로 setter 를 호출해서 필드에 값을 세팅
	 * 
	 * MemberDTO
	 * */
	
	// *** @ModelAttribute 를 이용해 값이 필드에 세팅된 객체를
	// "커맨드 객체" 라고 부름 ***
	
	// *** @ModelAttribute 사용 시 주의사항 ***
	// - DTO 에 기본생성자, setter 가 필수로 존재해야 한다.
	
	// *** @ModelAttribute 어노테이션은 생략이 가능 ***
	// import 안해도 돌아감
	/*
inputMember : MemberDTO(memberId=user02, memberPw=pass02, memberName=고길동, memberAge=53)
inputMember : MemberDTO(memberId=user03, memberPw=pass03, memberName=이순신, memberAge=60)
	 */
	@PostMapping("test4")
	public String paramTest4(/*@ModelAttribute*/ MemberDTO inputMember) {
		//                                                -> 커맨드 객체
		log.debug("inputMember : " + inputMember.toString());
		
		/*
inputMember : MemberDTO(memberId=user01, memberPw=pass01, memberName=홍길동, memberAge=20)
		 */
		
		return "redirect:/param/main";
	}
}
