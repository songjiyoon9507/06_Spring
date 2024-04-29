package edu.kh.project.board.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.kh.project.board.model.dto.Comment;
import edu.kh.project.board.model.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/* @RestController (REST API 구축을 위해서 사용하는 컨트롤러)
 * 
 * = @Controller(요청/응답 제어 + Bean 등록) + @ResponseBody(응답 본문으로 데이터 자체 반환)
 * 
 * -> 모든 응답을 응답 본문(ajax)으로 반환하는 컨트롤러
 *    (비동기 요청만을 받는 Controller)
 * */

@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

	// fetch - 비동기요청
	// "/comment" 요청이 오면 해당 컨트롤러에서 잡아서 처리함
	// @ResponseBody 를 매번 메서드에 추가
	
	private final CommentService service;
	
	/** 댓글 목록 조회
	 * @param boardNo
	 * @return commentList
	 */
	@GetMapping("")
	public List<Comment> select(@RequestParam("boardNo") int boardNo) {
		// HttpMessageConverter 가
		// List -> JSON (문자열) 로 변환해서 응답
		return service.select(boardNo);
	}
	
	/** 댓글/답글 등록
	 * @return result
	 */
	@PostMapping("")
	public int insert(@RequestBody Comment comment) {
		return service.insert(comment);
	}
	
	/** 댓글 수정
	 * @return result
	 */
	@PutMapping("")
	public int update(@RequestBody Comment comment) {
		return service.update(comment);
	}
	
	/** 댓글 삭제 (update)
	 * @return result
	 */
	@DeleteMapping("")
	public int delete(@RequestBody int commentNo) {
		return service.delete(commentNo);
	}
}
