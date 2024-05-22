package edu.kh.project.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import edu.kh.project.websocket.handler.ChattingWebsocketHandler;
import edu.kh.project.websocket.handler.TestWebsocketHandler;
import lombok.RequiredArgsConstructor;

@Configuration // 서버 실행 시 작성된 메서드를 모두 수행할 수 있게끔 해주는 어노테이션
@EnableWebSocket // 웹소켓 활성화 설정 어노테이션
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer{

	// Bean 으로 등록된 SessionHandshakeInterceptor 가 주입됨 (HandshakeInterceptor 자식)
	private final HandshakeInterceptor handshakeInterceptor;
	
	// 웹소켓 처리 동작이 작성된 객체 의존성 주입
	private final TestWebsocketHandler testWebsocketHandler;
	
	// 채팅관련 웹소켓 처리 동작이 작성된 객체 의존성 주입
	private final ChattingWebsocketHandler chattingWebsocketHandler;
	
	// 웹소켓 핸들러를 등록하는 메서드 (핸들러를 등록해줘야 이용할 수 있음)
	// 어떤 interceptor 이용해서 가로채왔는지도 알려줘야함
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// addHandler(웹소켓 핸들러, 웹소켓 요청 주소)
		// 자바스크립트에서 써줄 요청주소를 똑같이 써주면 됨
		
		registry.addHandler(testWebsocketHandler, "/testSock")
		// ws:// 웹소켓 프로토콜
		// ws://localhost/testSock 으로 클라이언트가 요청을 하면
		// testWebsocketHandler 가 처리하도록 등록하는 과정
		.addInterceptors(handshakeInterceptor)
		// 클라이언트 연결 시 HttpSession 을 가로채 핸들러에게 전달
		.setAllowedOriginPatterns("http://localhost/",
								"http://127.0.0.1/",
								"http://192.168.50.206/")
		// 웹 소켓 요청이 허용되는 ip/도메인 지정 (루프백 ip) 내 컴퓨터로만 접속 가능
		// 남들이 들어올 때 사용하려면 내 ip 주소도 적어줘야함
		.withSockJS();
		// javascript 에서 요청할 때 SockJS 연결
		
		registry.addHandler(chattingWebsocketHandler, "/chattingSock")
		.addInterceptors(handshakeInterceptor)
		.setAllowedOriginPatterns("http://localhost/",
				"http://127.0.0.1/",
				"http://192.168.50.206/")
		.withSockJS();
	}
}
