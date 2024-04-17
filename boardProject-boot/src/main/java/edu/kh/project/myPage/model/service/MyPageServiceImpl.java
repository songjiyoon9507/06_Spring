package edu.kh.project.myPage.model.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.common.util.Utility;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;
import edu.kh.project.myPage.model.mapper.MyPageMapper;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor=Exception.class) // 모든 예외 발생 시 롤백 rollbackFor 안 쓰면 runtimeException 까지만
@RequiredArgsConstructor
@PropertySource("classpath:/config.properties")
public class MyPageServiceImpl implements MyPageService {

	private final MyPageMapper mapper;

	// BCrypt 암호화 객체 의존성 주입(SecurityConfig 참고)
	private final BCryptPasswordEncoder bcrypt;
	
	@Value("${my.profile.web-path}")
	private String profileWebPath;
	
	@Value("${my.profile.folder-path}")
	private String profileFolderPath;
	
	// 회원 정보 수정
	@Override
	public int updateInfo(Member inputMember, String[] memberAddress) {
		
		// memberAddress 가공해줘야함
		// 입력된 주소가 있을 경우
		// memberAddress 를 우편주소^^^도로명지번/주소^^^상세주소 형태로 가공
		
		// 주소 입력 안했을 경우 null 대입
		// 주소 입력 X -> inputMember.getMemberAddress() -> ",,"
//		if(inputMember.getMemberAddress().equals(",,")) {
//			
//			// 주소에 null 대입
//			inputMember.setMemberAddress(null);
//		} else {
//			// memberAddress 를 가공
//			String address = String.join("^^^", memberAddress);
//			
//			// 주소에 가공된 데이터를 대입
//			inputMember.setMemberAddress(address);
//		}
		
		
		if(!inputMember.getMemberAddress().equals(",,")) { // 주소 입력 됐을 때
			String address = String.join("^^^", memberAddress);
			
			inputMember.setMemberAddress(address);
		} else {
			// 주소 입력 되지 않은 경우
			inputMember.setMemberAddress(null);
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

	// 파일 업로드 테스트1
	@Override
	public String fileUpload1(MultipartFile uploadFile) throws Exception {
		
		// MultipartFile 이 제공하는 메서드
		// - getSize() : 파일 크기
		// - isEmpty() : 업로드한 파일이 없을 경우 true 반환
		// - getOriginalFileName() : 원본 파일 명
		// - transferTo(경로) : 실제로 DB에 제대로 올라갔을 때 씀 if(result > 0)
		//   메모리 또는 임시 저장 경로에 업로드된 파일을
		//   원하는 경로에 전송(서버 어떤 폴더에 저장할지 지정)
		
		if( uploadFile.isEmpty() ) { // 업로드한 파일이 없을 경우
			return null;
		}
		
		// 업로드한 파일이 있을 경우
		// C:/uploadFiles/test/파일명 으로 서버에 저장
		uploadFile.transferTo(
					new File("C:\\uploadFiles\\test\\" + uploadFile.getOriginalFilename()));
		
		// 웹에서 해당 파일에 접근할 수 있는 경로를 반환
		
		// 서버 : C:\\uploadFiles\\test\\a.jpg
		// 웹 접근 주소 : /myPage/file/a.jpg
		
		return "/myPage/file/" + uploadFile.getOriginalFilename();
	}

	// 파일 업로드 + DB 저장 + 조회 파일 업로드 테스트2
	@Override
	public int fileUpload2(int memberNo, MultipartFile uploadFile) throws IOException {

		// 업로드된 파일이 없다면
		// == 선택된 파일이 없는 경우
		if(uploadFile.isEmpty()) {
			return 0;
		}
		
		/* BLOB DB에 파일 올릴 수 있지만 올리지 않음
		 * DB에 파일 저장이 가능은 하지만
		 * 파일 Data 가 클 수 도 있음
		 * DB 에 부하를 줄이기 위해서 파일 자체를 DB에 넣지는 않음
		 * 
		 * 1) DB에는 서버에 저장할 파일 경로를 저장
		 * 
		 * 2) DB 삽입/수정 성공 후 서버에 파일을 저장
		 * 
		 * 3) 만약에 파일 저장 실패 시
		 *    -> 예외 발생
		 *    -> @Transactional 을 이용해서 rollback 수행
		 * */
		
		// 업로드한 파일이 있을 경우
		// 1. 서버에 저장할 파일 경로 만들기
		// /myPage/file/... 이런 경로를 만들어서 DB에 넣을 거 insert update 먼저해야지 image 만 있으면 의미 업음
		
		// 파일이 저장될 서버 폴더 경로
		String folderPath = "C:\\uploadFiles\\test\\";
		
		// 클라이언트가 접근할 수 있는 주소도 만들어줘야함
		// 클라이언트가 파일이 저장된 폴더에 접근할 수 있는 주소
		String webPath = "/myPage/file/";
		// -> 주소만 쓴다고 되는 게 아니라 FileConfig 안에 두개를 연결해주는 구문 작성해둠
		
		// 2. DB에 전달할 데이터를 DTO로 묶어서 INSERT 호출하기
		// webPath, memberNo, 원본파일명, 변경된 파일명
		
		// 2-1. 변경된 파일명 년월일시분초_시퀀스넘버.확장자
		String fileRename = Utility.fileRename(uploadFile.getOriginalFilename());
		// static 메서드 불러줄 때는 클래스명.메서드명
		
		// DTO 클래스 만들 준비 끝나면 DTO 클래스 하나 만들어줌
//		UploadFile uf = new UploadFile();
//		uf.setMemberNo(memberNo);
//		uf.setFilePath(webPath);
//		uf.setFileOriginalName(uploadFile.getOriginalFilename());
//		uf.setFileRename(fileRename);
		
		// -> @Builder 이용하면 이런식으로 사용 안해도 됨
		
		// Builder 패턴을 이용해서 UploadFile 객체 생성
		// 장점 1) 반복되는 참조변수명, set 구문 생략
		// 장점 2) method chaining 을 이용해서 한 줄로 작성 가능
		UploadFile uf = UploadFile.builder()
						.memberNo(memberNo)
						.filePath(webPath)
						.fileOriginalName(uploadFile.getOriginalFilename())
						.fileRename(fileRename)
						.build();
		
		// Mapper 에 전달
		int result = mapper.insertUploadFile(uf);
		
		// 3. 삽입(INSERT) 성공 시 파일을 지정된 서버 폴더에 저장
		
		// 삽입 실패 시
		if(result == 0) return 0;
		
		// 삽입 성공 시
		
		// C:\\uploadFiles\\test\\변경된파일명 으로
		// 파일을 서버 컴퓨터에 저장
		
		uploadFile.transferTo(new File(folderPath + fileRename));
		
		return result;
		
		/* CheckedException : 예외 처리 필수
		 * 
		 * IOException 은 CheckedException
		 * 
		 * @Transactional 은 RuntimeException() 만 처리
		 * RuntimeException은 UnCheckedException
		 * 
		 * -> IOException 발생했을 때 rollback 못 시켜줌
		 * 
		 * 그래서 @Transactional(rollbackFor=Exception.class) 이거 꼭 써줘야함
		 * 모든 예외 발생 시 rollback 처리
		 * 
		 * 롤백할 예외 범위 수정
		 * 
		 * UnCheckedException : 예외 처리 필수 X
		 * */
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
			
			if(file.isEmpty()) { // 파일이 없으면 다음 파일
				continue;
			}
			
			// 파일이 있을 때
			// fileUpload2() 메서드 호출 (재활용)
			// -> 파일 하나 업로드 + DB INSERT
			result1 += fileUpload2(memberNo, file);
			
		}
		
		// 2. bbbList 처리 (처리 방법 똑같음)
		int result2 = 0;
		
		// 업로드된 파일이 없을 경우를 제외하고 업로드
		for(MultipartFile file : bbbList) {
			
			if(file.isEmpty()) { // 파일이 없으면 다음 파일
				continue;
			}
			
			// 파일이 있을 때
			// fileUpload2() 메서드 호출 (재활용)
			// -> 파일 하나 업로드 + DB INSERT
			result2 += fileUpload2(memberNo, file);
			
		}
		
		return result1 + result2;
	}

	// 프로필 이미지 변경
	@Override
	public int profile(MultipartFile profileImg, Member loginMember) throws Exception {

		// 수정할 경로
		String updatePath = null;
		
		// 변경명 저장
		String rename = null;
		
		// 업로드한 이미지가 있을 경우
		// - 있을 경우 : 수정할 경로 조합 (클라이언트 접근 경로+리네임파일명)
		if(!profileImg.isEmpty()) {
			// updatePath 조합
			
			// 1. 파일명 변경
			rename = Utility.fileRename(profileImg.getOriginalFilename());
			
			// 2. /myPage/profile/변경된파일명
			updatePath = profileWebPath + rename;
		}
		
		// 수정된 프로필 이미지 경로 + 회원 번호를 저장할 DTO 객체
		Member mem = Member.builder()
				.memberNo(loginMember.getMemberNo())
				.profileImg(updatePath)
				.build();
		
		// UPDATE 수행
		int result = mapper.profile(mem);
		
		if(result > 0) {
			// DB에 수정 성공 시
			// 프로필 이미지를 없앤 경우(NULL로 수정한 경우)를 제외
			// -> 업로드한 이미지가 있을 경우
			if(!profileImg.isEmpty()) {
				// 파일을 서버 지정된 폴더에 저장
				profileImg.transferTo(new File(profileFolderPath + rename));
			}
			
			// 세션 회원 정보에서 프로필 이미지 경로를
			// 업데이트한 경로로 변경
			loginMember.setProfileImg(updatePath);
		}
		return result;
	}
}
