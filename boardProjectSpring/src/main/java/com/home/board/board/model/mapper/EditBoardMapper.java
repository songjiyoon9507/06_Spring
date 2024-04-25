package com.home.board.board.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.home.board.board.model.dto.Board;

@Mapper
public interface EditBoardMapper {

	/** 게시글 작성 (BOARD 테이블 INSERT)
	 * @param inputBoard
	 * @return result
	 */
	int boardInsert(Board inputBoard);

}
