package com.home.board.common.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@PropertySource("classpath:/config.properties")
public class EmailConfig {

	// @Value : properties 에 작성된 내용 중 키가 일치하는 값을 얻어와 필드에 대입
	@Value("${spring.mail.username}")
	private String userName;
	
	@Value("${spring.mail.password}")
	private String password;
	
	@Bean
	public JavaMailSender javaMailSender() {
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		
		// mailSender 에서 이용할 각종 설정들 작성
		Properties prop = new Properties();
		// 전송 프로토콜 설정
		prop.setProperty("mail.transport.protocol", "smtp");
		// SMTP 서버 인증 사용할지 말지
		prop.setProperty("mail.smtp.auth", "true");
		// 안정한 연결 활성화 할지 말지
		prop.setProperty("mail.smtp.starttls.enable", "true");
		// mail 보낼 때 debug 사용 여부
		prop.setProperty("mail.debug", "true");
		// 신뢰할 수 있는 서버 주소 사용 smtp.gmail.com
		prop.setProperty("mail.smtp.ssl.trust","smtp.gmail.com");
		// 버전
		prop.setProperty("mail.smtp.ssl.protocols","TLSv1.2");
		
		// 구글 smtp 사용자 계정
		mailSender.setUsername(userName);
		mailSender.setPassword(password);
		// smtp 서버 호스트 설정
		mailSender.setHost("smtp.gmail.com");
		// SMTP 포트 587
		mailSender.setPort(587);
		mailSender.setDefaultEncoding("UTF-8");
		mailSender.setJavaMailProperties(prop);
		
		return mailSender;
	}
}
