package com.home.board.board.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.home.board.board.model.dto.Board;

public interface EditBoardService {

	/** 게시글 작성
	 * @param inputBoard
	 * @param images
	 * @return boardNo
	 */
	int boardInsert(Board inputBoard, List<MultipartFile> images);

}
