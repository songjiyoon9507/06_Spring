package com.home.board.websocket.handler;

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
import com.home.board.chatting.model.dto.Message;
import com.home.board.chatting.service.ChattingService;
import com.home.board.member.model.dto.Member;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChattingWebsocketHandler extends TextWebSocketHandler {

	private final ChattingService service;
	
	private Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {

		sessions.add(session);
		
		log.info("{} 연결됨", session.getId());
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

		sessions.remove(session);
		
		log.info("{} 연결 끊김", session.getId());
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		Message msg = objectMapper.readValue(message.getPayload(), Message.class);
		
		if(msg.getImage() != null) {
			String image = msg.getImage();
			msg.setImage(image);
		}
		
		int result = service.insertMessage(msg);

		if(result > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd hh.mm");
			msg.setSendTime(sdf.format(new Date()));
			
			for(WebSocketSession s : sessions) {
				HttpSession temp = (HttpSession)s.getAttributes().get("session");

				int loginMemberNo = ((Member)temp.getAttribute("loginMember")).getMemberNo();

				if(loginMemberNo == msg.getTargetNo() || loginMemberNo == msg.getSenderNo()) {

					String jsonData = objectMapper.writeValueAsString(msg);

					s.sendMessage(new TextMessage(jsonData));
				}
			}
		}

        for (WebSocketSession webSocketSession : sessions) {
            if (webSocketSession.isOpen()) {
                webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
            }
        }
    }

	private String saveImage(String image) {
		// TODO Auto-generated method stub
		return null;
	}
	
//	private void broadcastMessage(Message message) {
//		ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            String jsonMessage = objectMapper.writeValueAsString(message);
//            TextMessage textMessage = new TextMessage(jsonMessage);
//
//            for (WebSocketSession sess : sessions) {
//                if (sess.isOpen()) {
//                    sess.sendMessage(textMessage);
//                }
//            }
//        } catch (Exception e) {
//            log.error("Failed to broadcast message", e);
//        }
//	}
	
//	private String saveImage(String imageUrl) {
//		return savedImagePath;
//	}
}
