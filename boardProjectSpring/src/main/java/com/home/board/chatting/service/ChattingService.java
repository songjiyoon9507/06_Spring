package com.home.board.chatting.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartRequest;

import com.home.board.chatting.model.dto.ChattingRoom;
import com.home.board.chatting.model.dto.Message;
import com.home.board.member.model.dto.Member;

public interface ChattingService {

	/** 채팅방 목록 조회
	 * @param memberNo
	 * @return roomList
	 */
	List<ChattingRoom> selectRoomList(int memberNo);

    /** 채팅방번호 체크
     * @param map
     * @return chattingNo
     */
    int checkChattingNo(Map<String, Integer> map);

    /** 새로운 채팅방 생성
     * @param map
     * @return chattingNo
     */
    int createChattingRoom(Map<String, Integer> map);

    /** 읽음 표시 업데이트
     * @param paramMap
     * @return
     */
    int updateReadFlag(Map<String, Integer> paramMap);

    /** 메세지 조회
     * @param paramMap
     * @return
     */
    List<Message> selectMessageList( Map<String, Integer> paramMap );

	/** 채팅 상대 검색
	 * @param map 
	 * @return memberList
	 */
	List<Member> selectTarget(Map<String, Object> map);
	
	/** 채팅 입력
     * @param msg
     * @return
     */
    int insertMessage(Message msg);
    
	/** 이미지 업로드
	 * @param inputImage
	 * @return result
	 */
	int uploadImage(MultipartRequest inputImage);
}
