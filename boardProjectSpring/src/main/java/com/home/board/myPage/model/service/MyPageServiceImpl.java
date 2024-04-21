package com.home.board.myPage.model.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.home.board.common.util.Utility;
import com.home.board.member.model.dto.Member;
import com.home.board.myPage.model.dto.UploadFile;
import com.home.board.myPage.model.mapper.MyPageMapper;

import lombok.RequiredArgsConstructor;

@Transactional(rollbackFor = Exception.class)
@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

	private final MyPageMapper mapper;

	private final BCryptPasswordEncoder bcrypt;
	
	// 회원 정보 수정
	@Override
	public int updateInfo(Member inputMember, String[] memberAddress) {

		// memberAddress 가공
		// 입력된 주소가 없을 경우
		if(inputMember.getMemberAddress().equals(",,")) {
			inputMember.setMemberAddress(null);
		} else {
			String address = String.join("^^^", memberAddress);
			
			// 주소에 가공된 데이터를 대입
			inputMember.setMemberAddress(address);
		}
		
		// SQL 수행 후 결과 반환
		return mapper.updateInfo(inputMember);
	}

	// 비밀번호 수정
	@Override
	public int changePw(Map<String, Object> paramMap, int memberNo) {
		
		// 현재 로그인한 회원의 암호화된 비밀번호를 DB에서 조회
		String originPw = mapper.selectPw(memberNo);
		
		// 암호화된 비밀번호를 평문으로 바꿔서 비교해줘야함
		// 입력받은 현재 비밀번호와 (평문)
		// DB에서 조회한 비밀번호 비교(암호화)
		// BCryptPasswordEncoder.matches(평문, 암호화된 비밀번호)
		
		// 다를 경우
		if(!bcrypt.matches((String)paramMap.get("currentPw"), originPw)) {
			return 0;
		}
		
		// 같을 경우
		
		// 새 비밀번호를 암호화해서 업데이트
		// 새 비밀번호 암호화 진행
		String encPw = bcrypt.encode((String)paramMap.get("newPw"));
		paramMap.put("encPw", encPw);
		paramMap.put("memberNo", memberNo);
		
		return mapper.changePw(paramMap);
	}

	// 회원 탈퇴
	@Override
	public int secession(String memberPw, int memberNo) {

		String originPw = mapper.selectPw(memberNo);
		
		if(!bcrypt.matches(memberPw, originPw)) {
			return 0;
		}
		
		return mapper.secession(memberNo);
	}

	// 파일 업로드 테스트 1
	@Override
	public String fileUpload1(MultipartFile uploadFile) throws Exception {

		// MultipartFile 이 제공하는 메서드
		// - getSize() : 파일 크기
		// - isEmpty() : 업로드한 파일이 없을 경우 true
		// - getOriginalFileName() : 원본 파일 명
		// - transferTo(경로) : 
		// 		임시 저장 경로에서 원하는 진짜 경로로 전송하는 일
		//   메모리 또는 임시 저장 경로에 업로드된 파일을
		//   원하는 경로에 전송(서버 어떤 폴더에 저장할지 지정)
		
		// 업로드한 파일이 없을 경우
		if(uploadFile.isEmpty()) {
			return null;
		}
		
		// 업로드한 파일이 있을 경우
		// C:/uploadFiles/test/파일명 으로 서버에 저장 (이것도 마찬가지로 폴더가 존재해야함)
		uploadFile.transferTo(
					new File("C:\\uploadFiles\\test\\" + uploadFile.getOriginalFilename()));
		
		// 웹에서 해당 파일에 접근할 수 있는 경로를 반환
		
		// 서버 : C:\\uploadFiles\\test\\a.jpg
		// 웹 접근 주소 : /myPage/file/a.jpg
		
		return "/myPage/file/" + uploadFile.getOriginalFilename();
	}

	// 파일 업로드 TEST 2 파일 업로드 + DB 저장 + 조회
	@Override
	public int fileUpload2(MultipartFile uploadFile, int memberNo) throws IOException {
		
		// 업로드된 파일이 있는지 없는지 먼저 따져줄 거임
		// 선택된 파일이 없는 경우
		if(uploadFile.isEmpty()) {
			return 0; // DB 에 삽입된 게 없다
		}
		
		/* DB 에 BLOB
		 * DB 에 파일 저장이 가능하지만
		 * DB 부하를 줄이기 위해
		 * 
		 * DB에는 서버에 저장할 파일 경로를 저장함
		 * DB 삽입/수정 성공 후 서버에 파일을 저장
		 * 만약에 파일 저장 실패 시
		 * -> 예외 발생
		 * -> @Transactional 을 이용해서 rollback 수행
		 * */
		
		// 선택된 파일이 있을 경우
		// 1. 서버에 저장할 파일 경로 만들기

		// 파일이 저장될 서버 폴더 경로
		String folderPath = "C:\\uploadFiles\\test\\";
		
		// 클라이언트가 파일이 저장된 폴더에 접근할 수 있도록 해주는 주소
		String webPath = "/myPage/file/";
		
		// 위 둘은 registry 로 연결 FileConfig
		
		// 2. DB에 전달할 데이터를 DTO 로 묶어서 INSERT 호출하기
		// webPath, memberNo, 원본파일명, 변경된 파일명
		// Utility 클래스 프로그램 전체적으로 사용될 유용한 기능 모음 클래스
		String fileRename = Utility.fileRename(uploadFile.getOriginalFilename());
		
		// DTO 클래스 생성
		// Builder 패턴을 이용해서 UploadFile 객체 생성
		// 장점 1) 반복되는 참조변수명, set 구문 생략
		// 장점 2) method chaining 을 이용해서 한줄로 작성 가능
		UploadFile uf = UploadFile.builder()
						.memberNo(memberNo)
						.filePath(webPath)
						.fileOriginalName(uploadFile.getOriginalFilename())
						.fileRename(fileRename)
						.build();
		
		int result = mapper.insertUploadFile(uf);
		
		// 3. 삽입(INSERT) 성공 시 파일을 지정된 서버 폴더에 저장
		
		// 삽입 실패 시
		if(result == 0) return 0;
		
		// 삽입 성공 시
		
		// C:\\uploadFiles\\test\\변경된파일명 으로
		// 파일을 서버 컴퓨터에 저장
		
		uploadFile.transferTo(new File(folderPath + fileRename));
		// C:\\uploadFiles\\test\\20240421150314_00004.jpg
		
		// File 사용 시 IOException 발생
		// => CheckException
		
		// @Transactional 은 RuntimeException만 처리 => UncheckedException
		// @Transactional 이렇게만 작성하면 IOException 못 잡아줌
		// => rollbackFor 속성 이용해서 롤백할 예외 범위 수정해줘야함
		
		return result;
	}

	// 파일 목록 조회
	@Override
	public List<UploadFile> fileList() {
		return mapper.fileList();
	}

	// 여러 파일 업로드
	@Override
	public int fileUpload3(List<MultipartFile> aaaList, List<MultipartFile> bbbList, int memberNo) throws Exception {

		// 1. aaaList 처리
		int result1 = 0;
		
		// 업로드된 파일이 없을 경우를 제외하고 업로드
		for(MultipartFile file : aaaList) {
			
			if(file.isEmpty()) {
				continue;
				// 현재 접근한 파일이 isEmpty 면 다음 파일로 넘어가겠다.
			}
			
			// fileUpload2() 메서드 호출 (재활용)
			// -> 파일 하나 업로드 + DB INSERT
			result1 += fileUpload2(file, memberNo);
		}
		
		// 2. bbbList 처리
		int result2 = 0;
		
		// 업로드된 파일이 없을 경우를 제외하고 업로드
		for(MultipartFile file : bbbList) {
			
			if(file.isEmpty()) {
				continue;
				// 현재 접근한 파일이 isEmpty 면 다음 파일로 넘어가겠다.
			}
			
			// fileUpload2() 메서드 호출 (재활용)
			// -> 파일 하나 업로드 + DB INSERT
			result2 += fileUpload2(file, memberNo);
		}		
		
		return result1 + result2;
	}
}
