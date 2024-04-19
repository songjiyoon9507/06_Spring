package com.home.board.email.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.home.board.email.model.mapper.EmailMapper;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional(rollbackFor = Exception.class) // rollbackFor 작성 안하면 RuntimeException만 잡아줌
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

	// EmailConfig 설정이 적용된 객체(메일 보내기 기능)
	private final JavaMailSender mailSender;

	// 타임리프(템플릿 엔진)를 이용해서 html 코드 -> java 코드로 변환
	private final SpringTemplateEngine templateEngine;
	
	// Mapper 의존성 주입
	private final EmailMapper mapper;

	// 이메일 보내기
	@Override
	public String sendEmail(String htmlName, String email) {

		// 6자리 난수 생성
		String authKey = createAuthKey();
		
		// 이메일 보낼 때 Exception 발생할 수 있어서 try catch
		try {
			
			// 메일 제목
			String subject = null;
			
			// 키에 따라 메일 내용 다르게 보내기 위해
			switch(htmlName) {
			case "signup" :
				subject = "[boardProject] 회원 가입 인증번호 입니다."; break;
			}
			
			// 인증 메일 보내기
			
			// MimeMessage (객체) : Java 에서 메일을 보내는 객체
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			
			// MimeMessageHelper : Spring 에서 제공하는 메일 발송 도우미(간단 + 타임리프)
			MimeMessageHelper helper
				= new MimeMessageHelper(mimeMessage, true, "UTF-8");
			// 1번 매개변수 : MimeMessage 객체
			// 2번 매개변수 : 파일 전송 사용 할거냐(true) 말거냐(false)
			// 3번 매개변수 : 문자 인코딩 지정
			
			helper.setTo(email); // 받는 사람 이메일 지정 (회원가입하는사람)
			helper.setSubject(subject); // 이메일 제목 지정
			
			 // html
			helper.setText( loadHtml(authKey, htmlName), true);
			// HTML 코드 해석 여부 true (innerHTML 해석)
			
			// CID(Content-ID)를 이용해 메일에 이미지 첨부
			// (파일첨부와는 다름, 이메일 내용 자체에 사용할 이미지 첨부)
			// logo 추가예정
			helper.addInline("logo", new ClassPathResource("static/images/logo.jpg")); // html 에서 cid:logo 로 부름
			// -> 로고 이미지를 메일 내용에 첨부하는데
			//    사용하고 싶으면 "logo"라는 id 를 작성해라
			// classpath 는 resources	
			
			// 메일 보내기
			mailSender.send(mimeMessage);
			
			log.debug("인증번호 : " + authKey);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		// 이메일 + 인증번호를 "TB_AUTH_KEY" 테이블 저장
		Map<String, String> map = new HashMap<>();
		map.put("authKey", authKey);
		map.put("email", email);
		
		// 1) 해당 이메일이 DB에 존재하는 경우 (인증번호 받은 이력이 있는 경우)
		//    수정(update)을 먼저 진행
		//    -> 1 반환 == 업데이트 성공 == 이미 존재해서 인증번호 변경됐다.
		//    -> 0 반환 == 업데이트 실패 == 이메일이 테이블에 존재 X (처음 인증번호 받음) --> INSERT 시도
		
		int result = mapper.updateAuthKey(map);
		
		if(result == 0) {
			// 업데이트 실패 (인증번호 처음) INSERT 시도
			result = mapper.insertAuthKey(map);
			
		}
		
		// INSERT 후 result 가 0이면 실패, 1이면 성공
		if(result == 0) return null;
		
		// 성공
		return authKey; // 오류없이 완료되면 인증번호 반환
	}
	
	// HTML 파일을 읽어와 String 으로 변환 (타임리프 적용)
	private String loadHtml(String authKey, String htmlName) {

		// org.thymeleaf.Context. 선택
		Context context = new Context();
		// thymeleaf 가 적용된 html 상에서 값을 세팅할 수 있는 객체
		
		// 타임리프가 적용된 HTML 에서 사용할 값을 추가
		context.setVariable("authKey", authKey);
		
		// 어떤 HMTL 을 해석하고 있는지도 알려줘야함
		return templateEngine.process("email/" + htmlName, context);
		// templates/email 폴더에서 htmlName 과 같은 .html 파일 내용을 읽어와서 String 으로 변환
		// , context 는 authKey 세팅해둔 거 보내주는 거
	}

	/** 인증번호 생성 (영어 대문자 + 소문자 + 숫자 6자리)
	 * @return authKey
	 */
	public String createAuthKey() {
		
		String key = "";
		
		for(int i = 0 ; i < 6 ; i++) {
      
			int sel1 = (int)(Math.random() * 3); // 0:숫자 / 1,2:영어
  
				if(sel1 == 0) {
      
					int num = (int)(Math.random() * 10); // 0~9
					key += num;
      
				} else {
      
					char ch = (char)(Math.random() * 26 + 65); // A~Z
  
					int sel2 = (int)(Math.random() * 2); // 0:소문자 / 1:대문자
  
					if(sel2 == 0) {
						ch = (char)(ch + ('a' - 'A')); // 대문자로 변경
					}
					key += ch;
				}
		}
		return key;
	}

	// 이메일, 인증번호 확인
	@Override
	public int checkAuthKey(Map<String, Object> map) {
		return mapper.checkAuthKey(map);
	}
	
}