package edu.kh.project.common.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/* Filter : 요청, 응답 시 걸러내거나 추가할 수 있는 객체
 * 
 * [필터 역할을 하는 필터 클래스 생성 방법]
 * 1. jakarta.servlet.Filter 인터페이스를 상속 받아야함
 * 2. doFilter() 메서드를 오버라이딩 해야함 (필터가 어떤 동작을 할 것이냐 정의하는 메서드)
 * */

// 로그인이 되어있지 않은 경우 특정 페이지로 돌아가게 함
public class LoginFilter implements Filter{ // 1. jakarta.servlet.Filter 인터페이스를 상속 받아야함

	// ctrl + space
	// 2. doFilter() 메서드를 오버라이딩 해야함 (필터가 어떤 동작을 할 것이냐 정의하는 메서드)
	// 필터 동작을 정의하는 메서드
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		/* ServletRequest   : HttpServletRequest 의 부모 타입
		 * ServletResponse  : HttpServletResponse 의 부모 타입
		 * 
		 * Servlet 통신이 다 Http 가 아님
		 * 
		 * 우리는 Http 통신 사용
		 * Http 통신이 가능한 형태로 다운 캐스팅해서 사용해야함
		 * */
		
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		
		// Session 얻어오기 (request 에서 얻어옴)
		HttpSession session = req.getSession();
		
		// 세션에서 로그인한 회원 정보를 얻어오기
		// 얻어왔으나, 없을 때 -> 로그인이 되어있지 않은 상태
		if(session.getAttribute("loginMember") == null) { // 로그인 안된 상태
			// /loginError 요청 받아줄 컨트롤러 만들어서 그 쪽으로 재요청할 거임
			// resp 를 이용해서 원하는 곳으로 리다이렉트 시킬 거
			resp.sendRedirect("/loginError"); // localhost/loginError
			// 재요청 보낼 거임 resp 응답하는 거
		} else { // 로그인이 되어있는 경우
			
			// FilterChain (필터가 하나만 있는 게 아니라서 다음 필터로 넘어가던가)
			// - 다음 필터 또는 Dispatcher Servlet과 연결된 객체
			// 우리는 필터 하나임 Dispatcher Servlet 으로 바로 보내줄 거
			
			// 다음 필터로 요청/응답 객체 전달
			// (만약 없으면 Dispatcher Servlet 으로 전달)
			chain.doFilter(request, response);
			// 로그인 필터가 언제 적용되는지 설정해줘야함 filter.config 만들어서 설정해둘 거
		}
	}
}
