package edu.kh.project.board.model.service;

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

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.BoardImg;
import edu.kh.project.board.model.exception.BoardInsertException;
import edu.kh.project.board.model.exception.ImageDeleteException;
import edu.kh.project.board.model.exception.ImageUpdateException;
import edu.kh.project.board.model.mapper.EditBoardMapper;
import edu.kh.project.common.util.Utility;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
@PropertySource("classpath:/config.properties")
public class EditBoardServiceImpl implements EditBoardService {

	private final EditBoardMapper mapper;

	// config.properties 값을 얻어와 필드에 저장
	@Value("${my.board.web-path}")
	private String webPath; // /images/board/
	
	@Value("${my.board.folder-path}")
	private String folderPath; // C:/uploadFiles/board/
	
	// 게시글 작성
	@Override
	public int boardInsert(Board inputBoard, List<MultipartFile> images) throws IllegalStateException, IOException {

		// 1. 게시글 부분을 먼저
		//    BOARD 테이블 INSERT 하기
		//    -> INSERT 한 결과로 작성된 게시글 번호(생성된 시퀀스 번호) 반환 받기
		int result = mapper.boardInsert(inputBoard);
		
		// result == INSERT 결과 (0/1)
		
		// 삽입 실패 시
		if(result == 0) return 0;
		
		// 삽입된 게시글의 번호를 변수로 저장
		// -> mapper.xml 에서 <selectKey> 태그를 이용해서 생성된
		//    boardNo가 inputBoard 에 저장된 상태 (얕은 복사 개념 이해 필수)
		int boardNo = inputBoard.getBoardNo();
		
		// 2. 업로드된 이미지가 실제로 존재할 경우
		//    업로드된 이미지만 별도로 저장하여
		//    "BOARD_IMG" 테이블에 삽입하는 코드 작성
		// -> 리스트를 새로 만들어서 images 라는 리스트 비었는지 확인후 파일 있는 것만 세팅
		
		// 실제 업로드된 이미지의 정보를 모아둔 List 생성
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
						.boardNo(boardNo)
						.imgOrder(i)
						.uploadFile(images.get(i))
						.build();
				
				uploadList.add(img);
				
			}
		}
		
		// 선택한 파일이 없을 경우(제목이랑 내용은 보여줘야함)
		if(uploadList.isEmpty()) {
			return boardNo;
		}
		
		// 이미지 있다면 (image INSERT)
		// 선택한 파일이 존재할 경우
		// -> "BOARD_IMG" 테이블에 INSERT + 서버에 파일 저장
		
		// result == 삽입된 행의 개수 == uploadList.size()
		result = mapper.insertUploadList(uploadList);
		
		// 다중 INSERT 성공 확인 (uploadList에 저장된 값이 모두 정상 삽입 되었나)
		if(result == uploadList.size()) {
			
			// 성공 시 서버에 파일 저장 (uploadFiles)
			for(BoardImg img : uploadList) {
				img.getUploadFile().transferTo(new File(folderPath+img.getImgRename()));
			}
			
		} else {
			// 부분적으로 삽입 실패 -> 전체 서비스 실패로 판단
			// -> 이전에 삽입된 내용 모두 rollback
			
			// -> rollback 하는 방법
			// == RuntimeException 을 강제 발생
			// (@Transactional 에 의해 rollback 됨)
			
//			throw new RuntimeException(); -> 이렇게 쓰면 어디서 에러났는지 모름
			
			// 사용자 정의 예외
			throw new BoardInsertException("이미지가 정상 삽입되지 않음");
		}
		
		
		return boardNo;
	}

	// 게시글 수정
	@Override
	public int boardUpdate(Board inputBoard, List<MultipartFile> images, String deleteOrder) throws IllegalStateException, IOException {

		// 1. 게시글 (제목/내용) 부분 수정
		int result = mapper.boardUpdate(inputBoard);
		
		// 수정 실패 시 바로 return;
		if(result == 0) return 0;
		
		// ---------------------------------------------------
		
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

	@Override
	public int boardDelete(Board board) {
		
		int result = mapper.boardDelete(board);
		
		// update 실패시
		if(result == 0) {
			// 롤백
			throw new ImageDeleteException();
		}
		
		// 성공시 result return
		
		return result;
	}
}
