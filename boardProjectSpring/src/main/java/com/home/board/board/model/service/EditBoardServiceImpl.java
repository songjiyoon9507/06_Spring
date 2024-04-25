package com.home.board.board.model.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.home.board.board.model.dto.Board;
import com.home.board.board.model.dto.BoardImg;
import com.home.board.board.model.mapper.EditBoardMapper;
import com.home.board.common.util.Utility;

import lombok.RequiredArgsConstructor;

@Transactional(rollbackFor = Exception.class)
@Service
@RequiredArgsConstructor
public class EditBoardServiceImpl implements EditBoardService {

	private final EditBoardMapper mapper;

	// 게시글 작성
	@Override
	public int boardInsert(Board inputBoard, List<MultipartFile> images) {

		// 1. 게시글 부분을 먼저 BOARD 테이블 INSERT 하기
		// 넘겨받은 값 boardCode, memberNo, boardTitle, boardContent
		// insert 후 생성된 시퀀스인 boardNo 반환 받기
		int result = mapper.boardInsert(inputBoard);
		
		// sql 수행할 때 파라미터로 inputBoard 넘겨서 boardNo가 inputBoard에
		// 세팅돼서 반환됨(얕은 복사)
		// result 는 insert 성공했는지 실패했는지가 반환됨
		if(result == 0) return 0;
		int boardNo = inputBoard.getBoardNo();

		// 빈 리스트 하나 생성해서 받아온 images 리스트에 빈 게 있는 지 검사 후
		// 들어있는 것들만 uploadList 에 넣어줌
		List<BoardImg> uploadList = new ArrayList<>();

		for(int i = 0 ; i < images.size() ; i++) {
			
			// 파일이 존재할 때 uploadList 에 넣어주기
			if(!images.isEmpty()) {
				// 넣어줄 때 MultipartFile 형의 List 를 BoardImg 에
				// 삽입하기 위해 가공해서 넣어주기
				// 요청 경로, 원본명, 변경명
				
				// 원본명
				String originalName = images.get(i).getOriginalFilename();
				
				// 변경명
				String rename = Utility.fileRename(originalName);
				
				// IMG_ORDER == i (인덱스 == 순서)
				
				// 모든 값을 저장할 DTO 생성해서 uploadList 에 넣어줄 거

			}
		}
		
		
		
		return 0;
	}
}
