package com.home.board.board.model.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.home.board.board.model.dto.Board;
import com.home.board.board.model.dto.BoardImg;
import com.home.board.board.model.exception.BoardInsertException;
import com.home.board.board.model.mapper.EditBoardMapper;
import com.home.board.common.util.Utility;

import com.home.board.board.model.exception.ImageDeleteException;
import com.home.board.board.model.exception.ImageUpdateException;
import lombok.RequiredArgsConstructor;

@Transactional(rollbackFor = Exception.class)
@Service
@RequiredArgsConstructor
@PropertySource("classpath:/config.properties")
public class EditBoardServiceImpl implements EditBoardService {

	private final EditBoardMapper mapper;
	
	@Value("${my.board.web-path}")
	private String webPath; // /images/board/
	
	@Value("${my.board.folder-path}")
	private String folderPath; // C:/uploadFiles/board/

	// 게시글 작성
	@Override
	public int boardInsert(Board inputBoard, List<MultipartFile> images) throws IllegalStateException, IOException {

		// 1. 게시글 부분을 먼저 BOARD 테이블 INSERT 하기
		// 넘겨받은 값 boardCode, memberNo, boardTitle, boardContent
		// insert 후 생성된 시퀀스인 boardNo 반환 받기
		int result = mapper.boardInsert(inputBoard);
		
		// sql 수행할 때 파라미터로 inputBoard 넘겨서 boardNo가 inputBoard에
		// 세팅돼서 반환됨(얕은 복사)
		// result 는 insert 성공했는지 실패했는지가 반환됨
		if(result == 0) {
			return 0;
		}
		
		int boardNo = inputBoard.getBoardNo();

		// 빈 리스트 하나 생성해서 받아온 images 리스트에 빈 게 있는 지 검사 후
		// 들어있는 것들만 uploadList 에 넣어줌
		List<BoardImg> uploadList = new ArrayList<>();

		for(int i = 0 ; i < images.size() ; i++) {
			
			// 파일이 존재할 때 uploadList 에 넣어주기
			if(!images.get(i).isEmpty()) {
				// 넣어줄 때 다른 정보들도 같이 넣어줘야함
				// images 에 들어있는 MultipartFile 도 uploadFile 에 넣어줌
				
				// 원본명
				String originalName = images.get(i).getOriginalFilename();
				
				// 변경명
				String rename = Utility.fileRename(originalName);
				
				// IMG_ORDER == i (인덱스 == 순서)
				
				// 모든 값을 저장할 DTO 생성해서 uploadList 에 넣어줄 거
				BoardImg img = BoardImg.builder()
						.imgOriginalName(originalName)
						.imgRename(rename)
						.imgPath(webPath)
						.boardNo(boardNo)
						.imgOrder(i)
						.uploadFile(images.get(i))
						.build();
				
				uploadList.add(img);
			}
		}
		
		// 선택한 파일이 없을 경우
		if(uploadList.isEmpty()) return boardNo;
		
		// 이미지가 있다면
		result = mapper.insertUploadList(uploadList);
		
		if(result == uploadList.size()) {
			for(BoardImg img : uploadList) {
				img.getUploadFile().transferTo(new File(folderPath+img.getImgRename()));
				
				// MultipartFile 이 제공하는 메서드
				// - getSize() : 파일 크기
				// - isEmpty() : 업로드한 파일이 없을 경우 true 반환
				// - getOriginalFileName() : 원본 파일 명
				// - transferTo(경로) : 실제로 DB에 제대로 올라갔을 때 씀 if(result > 0)
				//   메모리 또는 임시 저장 경로에 업로드된 파일을
				//   원하는 경로에 전송(서버 어떤 폴더에 저장할지 지정)
			}
		} else {
			throw new BoardInsertException("이미지 정상 삽입되지 않음");
		}
		
		return boardNo;
	}

	// 게시글 수정
	@Override
	public int boardUpdate(Board inputBoard, List<MultipartFile> images, String deleteOrder) throws IllegalStateException, IOException {
		
		int result = mapper.boardUpdate(inputBoard);
		
		if(result == 0) return 0;
		
		// 2. 기존에 이미지가 있었는데 없어진 경우
		//    (삭제된 이미지(deleteOrder)가 있는 경우)
		if(deleteOrder != null && !deleteOrder.equals("")) {
			Map<String, Object> map = new HashMap<>();
			map.put("deleteOrder", deleteOrder);
			map.put("boardNo", inputBoard.getBoardNo());
					
			result = mapper.deleteImage(map);
		}
				
		// 삭제 실패 시 (부분 실패 포함)
		if(result == 0) {
			throw new ImageDeleteException();
		}
				
		// 3. 선택한 파일이 존재할 경우
		//    해당 파일 정보만 모아두는 List 생성
		List<BoardImg> uploadList = new ArrayList<>();
				
		// images 리스트에서 하나씩 꺼내어 선택된 파일이 있는지 검사
		for(int i = 0 ; i < images.size() ; i++) {
					
			// 실제 선택된 파일이 존재하는 경우
			if( !images.get(i).isEmpty() ) {
						
				// 원본명
				String originalName = images.get(i).getOriginalFilename();
						
				// 변경명
				String rename = Utility.fileRename(originalName);
						
				// IMG_ORDER == i (인덱스 == 순서)
						
				// 모든 값을 저장할 DTO 생성 (BoardImg - Builder 패턴 사용)
				BoardImg img = BoardImg.builder()
								.imgOriginalName(originalName)
								.imgRename(rename)
								.imgPath(webPath)
								.boardNo(inputBoard.getBoardNo())
								.imgOrder(i)
								.uploadFile(images.get(i))
								.build();
						
				uploadList.add(img);
						
				// 4. 업로드 하려는 이미지 정보(img)를 이용해서
				//    수정 또는 삽입 수행
						
				// 1) 기존에 있을 때 -> 새 이미지로 변경하면 -> 수정 성공
				result = mapper.updateImage(img);
						
				if(result == 0) {
					// 수정 실패 == 기존 해당 순서(IMG_ORDER)에 이미지가 없었음
					// (행이 없으면) -> 삽입 수행
					// 일치하는 행이 있으면 업데이트 해라 라고 명령을 보내서
					// 행이 없으면 0 이 반환됨
							
					// 2) 기존에 없었던 애 -> 새 이미지 추가
					result = mapper.insertImage(img);
				}
			}
					
			// 수정 또는 삽입이 실패한 경우
			if(result == 0) {
				throw new ImageUpdateException(); // 예외 발생 -> 롤백
			}
					
		}
				
		// 선택한 파일이 없을 경우
		if(uploadList.isEmpty()) {
			return result;
		}
				
		// 수정, 새 이미지 파일을 서버에 저장
		for(BoardImg img : uploadList) {
			img.getUploadFile().transferTo(new File(folderPath+img.getImgRename()));
		}
				
		return result;
	}
}
