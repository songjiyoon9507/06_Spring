package edu.kh.project.websocket.handler;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.kh.project.chatting.model.dto.Message;
import edu.kh.project.chatting.model.service.ChattingService;
import edu.kh.project.member.model.dto.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component // Bean 등록
@Slf4j
@RequiredArgsConstructor
public class ChattingWebsocketHandler extends TextWebSocketHandler {

	private final ChattingService service;
	
	private Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
	
	// 클라이언트와 연결이 완료되고, 통신할 준비가 되면 실행되는 메서드
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {

		sessions.add(session);
		
		log.info("{} 연결됨", session.getId());
	}
	
	// 클라이언트와 연결이 종료되면 실행되는 메서드
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

		sessions.remove(session);
		
		log.info("{} 연결 끊김", session.getId());
	}
	
	// 클라이언트로부터 텍스트 메세지를 받았을 때 실행하는 메서드
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

		// 1:1 채팅할 수 있게 설정
		
		// 1. message 데이터 가공
		// message - JS 에서 전달받은 내용
		// senderNo, targetNo, chattingNo, messageContent 가 들어있음
		// {"senderNo" : "1", "targetNo" : "2", "chattingNo" : "8", messageContent : "Hi"}
		// JSON 형태로 온 obj 를 Message DTO 형태로 변환해서 사용할 것
		
		// Jackson 에서 제공하는 객체 (Spring Boot 이용시 자동 추가돼있음)
		ObjectMapper objectMapper = new ObjectMapper();
		
		Message msg = objectMapper.readValue(message.getPayload(), Message.class);
		// JSON 형태 message 를 읽어서 Message 클래스 DTO 형태로 변환 후 msg 에 담아줌
		
		// Message 객체 확인
		log.info("msg : {}", msg);
		
		// 2. DB 에 채팅 메세지 insert 해줘야함 service 연결 후 서비스 호출
		int result = service.insertMessage(msg);
		
		// 3. 성공한 행의 개수 돌려 받아서 분기 처리
		if(result > 0) {
			// Message DTO 에는 sendTime 이 있는데 JS에서 받아온 값에는 없음
			// 값 넣어줄 거임
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd hh.mm");
			msg.setSendTime(sdf.format(new Date()));
			// java.util 이용해서 지금 시간 세팅
			
			// 누구한테 보낼 것인지 처리해줘야함
			// sessions 안에 있음 현재 로그인한 사람의 대상 번호
			// 전역변수로 선언된 sessions 에는 현재 웹 소켓에 접속 중인 모든 회원의 세션 정보가 담겨져 있음
			for(WebSocketSession s : sessions) {
				
				// 가로챈 session 꺼내기
				HttpSession temp = (HttpSession)s.getAttributes().get("session");
				// 캐스팅해서 넣어줘야함
				
				// 로그인된 회원 정보 중 회원 번호를 꺼내오기
				int loginMemberNo = ((Member)temp.getAttribute("loginMember")).getMemberNo();
				
				// 누구한테 메세지를 보낼 건지 따져줘야함
				// 로그인 상태인 회원 중 targetNo가 일치하는 회원에게 메세지 전달
				if(loginMemberNo == msg.getTargetNo() || loginMemberNo == msg.getSenderNo()) {
					// 보낸 사람이거나 받은 사람일 때 메세지를 전달해주겠다.
					
					// 다시 DTO(VO) Object 를 JSON 으로 변환 (js 에 보내야하니까)
					String jsonData = objectMapper.writeValueAsString(msg);
					// JSON String 형태 msg 를 쓴다
					s.sendMessage(new TextMessage(jsonData));
				}
				
			}
		}
		
	}
	
}
