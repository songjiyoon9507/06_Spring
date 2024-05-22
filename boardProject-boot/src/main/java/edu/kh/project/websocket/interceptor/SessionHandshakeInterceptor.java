package edu.kh.project.websocket.interceptor;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import jakarta.servlet.http.HttpSession;

/* SessionHandshakeInterceptor
 * 
 * WebSocketHandler가 동작하기 전/후에
 * 연결된 클라이언트 세션을 가로채는 동작을 작성할 클래스
 * */
@Component // Bean 으로 등록해야함 웹소켓 통신할 때 interceptor 가 필요한 곳에 주입돼서 사용되려면 꼭 등록돼있어야함
public class SessionHandshakeInterceptor implements HandshakeInterceptor {

	// 현재 접속한 클라이언트 Session 이 무엇인지 가로채기 먼저 해야함
	// 처음에 HTTP 통신으로 요청 보냄 그걸 웹소켓 연결 설정하기 위해 필요한 interface

	// 핸들러 동작 전에 수행되는 메서드
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		
		// ServerHttpRequest  : HttpServletRequest 의 부모 인터페이스
		// ServerHttpResponse : HttpServletResponse 의 부모 인터페이스
		
		// attributes : 해당 맵에 세팅된 속성(데이터)은 다음에 동작할 Handler 객체에게 전달됨
		//              (HandshakeInterceptor -> Handler 데이터 전달하는 역할)
		
		// 다운캐스팅 중 예외 발생할 수 있음
		// ServletServerHttpRequest -> ServerHttpRequest 의 자식 => 여기서 session 을 뽑아와야함
		// request 가 참조하는 객체가
		// ServletServerHttpRequest 로 다운캐스팅이 가능한가를 처리해주는 if문 작성
		if(request instanceof ServletServerHttpRequest) {
			
			// 다운 캐스팅
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest)request;
			
			// 웹 소켓 동작을 요청한 클라이언트의 세션을 얻어올 것
			HttpSession session = servletRequest.getServletRequest().getSession();
			
			// 가로챈 세션을 Handler 에 전달할 수 있게 값을 세팅
			attributes.put("session", session);
		}
		
		// 가로채기 할지 말지 여부를 작성하는 곳
		// true 로 작성해야 세션을 가로채서 Handler 에게 전달 가능 (기본값이 false)
		return true;
	}
	
	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		// TODO Auto-generated method stub
		
	}
	
}
