package edu.kh.project.common.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@PropertySource("classpath:/config.properties")
@Configuration
public class EmailConfig {

	// @Value : properties 에 작성된 내용 중 키가 일치하는 값을 얻어와 필드에 대입
	@Value("${spring.mail.username}") // factory.annotation
	private String userName;
	
	@Value("${spring.mail.password}")
	private String password;
	
	@Bean
	public JavaMailSender javaMailSender() {
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		
		Properties prop = new Properties();
		prop.setProperty("mail.transport.protocol", "smtp");
		// 전송 프로토콜 설정
		prop.setProperty("mail.smtp.auth", "true");
		// SMTP 서버 인증 사용할지 말지
		prop.setProperty("mail.smtp.starttls.enable", "true");
		// 안정한 연결 활성화 할지 말지
		prop.setProperty("mail.debug", "true");
		// mail 보낼 때 debug 사용 여부
		prop.setProperty("mail.smtp.ssl.trust","smtp.gmail.com");
		// 신뢰할 수 있는 서버 주소 사용 smtp.gmail.com
		prop.setProperty("mail.smtp.ssl.protocols","TLSv1.2");
		// 버전
		
		mailSender.setUsername(userName);
		// 구글 smtp 사용자 계정
		mailSender.setPassword(password);
		mailSender.setHost("smtp.gmail.com");
		// smtp 서버 호스트 설정
		mailSender.setPort(587);
		// SMTP 포트 587
		mailSender.setDefaultEncoding("UTF-8");
		mailSender.setJavaMailProperties(prop);
		
		return mailSender;
	}
}
