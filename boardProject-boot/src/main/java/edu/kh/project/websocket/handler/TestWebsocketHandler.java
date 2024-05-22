package edu.kh.project.websocket.handler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

/** OOOWebsocketHandler 클래스
 * 
 * 웹소켓 동작 시 수행할 구문을 작성하는 클래스
 */
@Slf4j
@Component // Bean 등록 필요한 곳에 주입되어 사용돼야함 (의존성 주입 받아)
public class TestWebsocketHandler extends TextWebSocketHandler {

	// interceptor 클래스, handler 클래스 에 관한 설정 파일도 만들어야함
	
	// Set 중복 안됨 순서 유지 X (-> session 이 중복돼서 들어오지 않음)
	
	// WebSocketSession -> 웹소켓 연결을 나타내는 객체 클라이언트와 서버간 개별적 연결을 나타내는 객체
	// 클라이언트와 서버 간에 전이중 통신을 담당하는 객체 (양방향 왔다갔다할 수 있는 통신)
	// 이전에 만들어둔 SessionHandshakeInterceptor 가 가로챈 연결한 클라이언트의 HttpSession 값을 가지고 있음
	// -> attributes 에 추가한 값
	
	// 동기화된 Set 생성
	/* 동기와 비동기의 차이
	 * 비동기는 하나의 작업이 끝나기 전에 작업을 시작하는 것
	 * 동기는 하나의 작업이 끝나면 작업을 시작하는 것
	 * */
	
	// Collections.synchronizedSet
	private Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
	// 웹소켓 통신할 때 여러 명 클라이언트가 들어올 수 있음 모든 애들의 session 가져와서 서버에서 수집하기 위해 만듦
	// 요청을 보냈는데 동기로 받으면 session 꼬이는 순간이 발생함
	// -> 여러 클라이언트가 동시에 연결되고 동시에 종료되게 (순서 꼬이지않고) 데이터 일관성있게 하기 위해
	// 여러 가지 스레드가 동작하는 환경에서 하나의 컬렉션에 여러 스레드가 접근하여 의도치 않은 문제가 발생되지 않게
	// 하기 위해서 동기화를 진행하여 스레드가 순서대로 한 컬렉션에 접근할 수 있도록 변경. (보호하는 역할)
	
	// 클라이언트와 연결이 완료되고, 통신할 준비가 되면 실행하는 메서드
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {

		// session 은 Interceptor 에서 가로채 온 session 이 매개변수로 들어온 거
		// -> 가로채온 session 을 set 에 추가할 거
		// 연결된 클라이언트의 WebSocketSession 정보를 Set 에 추가
		// -> 웹소켓에 연결된 클라이언트 정보를 모아둠
		sessions.add(session);
	}
	
	// 클라이언트와 연결이 종료되면 실행하는 메서드 (채팅방 나간 경우 sessions 에서 session 빼줘야함)
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		
		// 웹 소켓 연결이 끊긴 클라이언트의 정보를 Set 에서 제거
		sessions.remove(session);
	}
	
	// 클라이언트로부터 텍스트 메세지를 받았을 때 실행하는 메서드
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

		// TextMessage : 웹소켓으로 연결된 클라이언트가 전달한 텍스트 (내용) 가 담겨있는 객체
		
		// message.getPayload() : 통신시 탑재된 데이터 (메세지 텍스트 자체)
		log.info("전달 받은 메세지 : {}", message.getPayload());
		
		// 전달 받은 메세지를 현재 해당 웹소켓에 연결된 모든 클라이언트에게 보내기
		// 하나씩 순차적으로 접근해줘야함
		for(WebSocketSession s : sessions) {
			s.sendMessage(message);
		}
	}
	
}

/*
WebSocketHandler 인터페이스 :
	웹소켓을 위한 메소드를 지원하는 인터페이스
	-> WebSocketHandler 인터페이스를 상속받은 클래스를 이용해 웹소켓 기능을 구현

WebSocketHandler 주요 메소드
     
	void handlerMessage(WebSocketSession session, WebSocketMessage message)
	- 클라이언트로부터 메세지가 도착하면 실행
	
	void afterConnectionEstablished(WebSocketSession session)
	- 클라이언트와 연결이 완료되고, 통신할 준비가 되면 실행
	
	void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus)
	- 클라이언트와 연결이 종료되면 실행
	
	void handleTransportError(WebSocketSession session, Throwable exception)
	- 메세지 전송중 에러가 발생하면 실행
 
----------------------------------------------------------------------------

TextWebSocketHandler : 
	WebSocketHandler 인터페이스를 상속받아 구현한
	텍스트 메세지 전용 웹소켓 핸들러 클래스 (이미지 없이 텍스트만 주고 받을 때)
	
	handlerTextMessage(WebSocketSession session, TextMessage message)
	- 클라이언트로부터 텍스트 메세지를 받았을때 실행
  
BinaryWebSocketHandler:
	WebSocketHandler 인터페이스를 상속받아 구현한
	이진 데이터 메시지를 처리하는 데 사용.
	주로 바이너리 데이터(예: 이미지, 파일)를 주고받을 때 사용.
*/
