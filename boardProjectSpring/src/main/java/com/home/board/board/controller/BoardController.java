package com.home.board.board.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.home.board.board.model.dto.Board;
import com.home.board.board.model.dto.BoardImg;
import com.home.board.board.model.service.BoardService;
import com.home.board.member.model.dto.Member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("board")
@Slf4j
@RequiredArgsConstructor
public class BoardController {

	private final BoardService service;
	
	@GetMapping("{boardCode:[0-9]+}")
	public String selectBoardList(@PathVariable("boardCode") int boardCode,
			@RequestParam(value="cp", required=false, defaultValue="1") int cp,
			Model model) {
		
		log.debug("boardCode : " + boardCode);
		
		Map<String, Object> map = service.selectBoardList(boardCode, cp);
		
		model.addAttribute("pagination", map.get("pagination"));
		model.addAttribute("boardList", map.get("boardList"));
		
		return "board/boardList";
	}
	
	@GetMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}")
	public String boardDetail(@PathVariable("boardCode") int boardCode,
			@PathVariable("boardNo") int boardNo,
			Model model,
			RedirectAttributes ra,
			@SessionAttribute(value="loginMember", required=false) Member loginMember,
			HttpServletRequest req,
			HttpServletResponse resp) {
		
		Map<String, Integer> map = new HashMap<>();
		map.put("boardCode", boardCode);
		map.put("boardNo", boardNo);
		
		if(loginMember != null) {
			map.put("memberNo", loginMember.getMemberNo());
		}
		
		Board board = service.selectOne(map);
		
		String path = null;
		
		// 조회 결과가 없는 경우
		if(board == null) {
			path = "redirect:/board/" + boardCode; // 목록 재요청
			ra.addFlashAttribute("message", "게시글이 존재하지 않습니다.");
		} else {
			/* **************** 쿠키를 이용한 조회 수 증가 (시작) ********************* */
			
			// 1. 비회원 또는 로그인한 회원의 글이 아닌 경우
			//    (글쓴이를 뺀 다른 사람)
			
			if(loginMember == null || loginMember.getMemberNo() != board.getMemberNo()) {
				// 로그인한 사람이 없거나 글쓴이가 아닌 경우
				
				// 요청에 담겨있는 모든 쿠키 얻어오기
				Cookie[] cookies = req.getCookies();
				
				Cookie c = null;
				
				for(Cookie newjeans : cookies) {
					
					// 요청에 담긴 쿠키에 "readBoardNo"가 존재할 때
					if(newjeans.getName().equals("readBoardNo")) {
						c = newjeans;
						break;
					}
				}
				
				int result = 0; // 조회수 증가 결과를 저장할 변수
				
				// "readBoardNo"가 현재 쿠키에 없을 때
				// (게시글 상세조회 했을 때 쿠키 없으면 이게 처음 보는 게시글)
				if(c == null) {
					
					// 새 쿠키 생성 ("readBoardNo", [게시글번호]) (K:V)형태
					c = new Cookie("readBoardNo", "[" + boardNo + "]");
					result = service.updateReadCount(boardNo);
					
				// "readBoardNo"가 현재 쿠키에 있을 때
				// "readBoardNo" : [2][400][1234][214]
				} else {
					// 누적해서 Cookie 에 쌓아주면 됨
					// 이미 Cookie 에 boardNo 가 존재하면 안 쌓아줄거임
					
					// 현재 글을 처음 읽은 경우
					if(c.getValue().indexOf("[" + boardNo + "]") == -1) {
						
						// 해당 글 번호를 쿠키에 누적 + 서비스 호출
						c.setValue(c.getValue() + "[" + boardNo + "]");
						result = service.updateReadCount(boardNo);
					}
					
				}
				
				// 조회 수 증가 성공 / 조회 성공 시
				if(result > 0) {
					
					// 먼저 조회된 board 객체의 readCount 값을 result 값으로 재대입
					board.setReadCount(result);
					
					// 적용 경로 설정
					c.setPath("/"); // "/" 최상위 경로 이하 요청 시 쿠키 서버로 전달
					
					// 수명 지정
					
					// 현재 시간을 얻어오기
					LocalDateTime now = LocalDateTime.now();
					
					// 다음 날 자정
					LocalDateTime nextDayMidnight = now.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
				
					// 다음 날 자정까지 남은 시간 계산 (초 단위)
					long secondsUntilNextDay = Duration.between(now, nextDayMidnight).getSeconds();
					
					// 쿠키 수명 설정
					c.setMaxAge((int)secondsUntilNextDay);
					
					resp.addCookie(c); // 응답 객체를 이용해서 클라이언트에게 전달
				}
				
			}
			
			/* **************** 쿠키를 이용한 조회 수 증가 (끝)   ********************* */
			
			
			
			// 조회 결과가 있는 경우
			path = "board/boardDetail";
			
			model.addAttribute("board", board);
			
			// 이미지가 있는 경우
			if(!board.getImageList().isEmpty()) {
				BoardImg thumbnail = null;
				
				// imgOrder 가 0 일 때 썸네일
				if(board.getImageList().get(0).getImgOrder() == 0) {
					thumbnail = board.getImageList().get(0);
				}
				
				model.addAttribute("thumbnail", thumbnail);
				model.addAttribute("start", thumbnail != null ? 1 : 0);
				// 썸네일이 null 이 아니면 start 는 1 썸네일 다음 이미지부터 보여줄 거
			}
			
		}
		
		return path;
	}
	
	/** 게시글 좋아요 체크/해제
	 * @param map
	 * @return count
	 */
	@ResponseBody
	@PostMapping("like")
	public int boardLike(@RequestBody Map<String, Integer> map) {
		return service.boardLike(map);
	}

}
