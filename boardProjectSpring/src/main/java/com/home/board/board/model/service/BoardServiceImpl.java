package com.home.board.board.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.home.board.board.model.dto.Board;
import com.home.board.board.model.dto.Pagination;
import com.home.board.board.model.mapper.BoardMapper;

import lombok.RequiredArgsConstructor;

@Transactional(rollbackFor = Exception.class)
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

	private final BoardMapper mapper;
	
	// 게시판 종류 조회
	@Override
	public List<Map<String, Object>> selectBoardTypeList() {
		return mapper.selectBoardTypeList();
	}

	// 특정 게시판의 지정된 페이지 목록 조회
	@Override
	public Map<String, Object> selectBoardList(int boardCode, int cp) {

		int listCount = mapper.getListCount(boardCode);
		
		Pagination pagination = new Pagination(cp, listCount);
		
		int limit = pagination.getLimit();
		int offset = (cp - 1) * limit;
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		List<Board> boardList = mapper.selectBoardList(boardCode, rowBounds);
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("pagination", pagination);
		map.put("boardList", boardList);
		
		return map;
	}

	// 게시글 상세 조회
	@Override
	public Board selectOne(Map<String, Integer> map) {
		return mapper.selectOne(map);
	}

	// 게시글 좋아요 체크/해제
	@Override
	public int boardLike(Map<String, Integer> map) {
		
		int result = 0;
		
		// 1. 좋아요가 체크된 상태인 경우 (likeCheck == 1)
		// -> BOARD_LIKE 테이블에 DELETE
		if(map.get("likeCheck") == 1) {
			result = mapper.deleteBoardLike(map);
		} else {
			// 2. 좋아요가 해제된 상태인 경우 (likeCheck == 0)
			// -> BOARD_LIKE 테이블에 INSERT
			result = mapper.insertBoardLike(map);
		}
		
		// 3. 다시 해당 게시글의 좋아요 개수 조회해서 반환
		if(result > 0) {
			return mapper.selectLikeCount(map.get("boardNo"));
		}
		
		return -1;
	}

	// 조회수 증가
	@Override
	public int updateReadCount(int boardNo) {
		
		// 1. 조회 수 1 증가
		int result = mapper.updateReadCount(boardNo);
		
		// 2. 현재 조회 수 조회
		if(result > 0) {
			return mapper.selectReadCount(boardNo);
		}
		
		return -1; // 실패한 경우 -1 반환
	}

}
