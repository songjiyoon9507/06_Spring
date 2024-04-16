package edu.kh.project.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.MultipartConfigElement;

@PropertySource("classpath:/config.properties") // 이 경로에 있는 거 가져다 쓰겠다.
@Configuration // 설정용 파일임을 알려줌
public class FileConfig implements WebMvcConfigurer {

	// WebMvcConfigurer : Spring MVC 프레임워크에서 제공하는 인터페이스 중 하나로,
	// 스프링 구성을 커스터마이징하고 확장하기 위한 메서드를 제공함.
	// 주로 웹 어플리케이션의 설정을 조정하거나 추가하는데 사용됨
	
	// 파일 업로드 임계값
	@Value("${spring.servlet.multipart.file-size-threshold}")
	private long fileSizeThreshold;
	
	// 요청당 파일 최대 크기
	@Value("${spring.servlet.multipart.max-request-size}")
	private long maxRequestSize;
	
	// 개별 파일당 최대 크기
	@Value("${spring.servlet.multipart.max-file-size}")
	private long maxFileSize;
	
	// 임계값 초과 시 임시 저장 폴더 경로
	@Value("${spring.servlet.multipart.location}")
	private String location;
	
	// ctrl + space
	// 요청 주소에 따라
	// 서버 컴퓨터의 어떤 경로에 접근할지 설정
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/myPage/file/**") // 클라이언트 요청 주소 패턴
		.addResourceLocations("file:///C:/uploadFiles/test/");
		// 클라이언트가 /myPage/file/** 패턴으로 이미지를 요청할 때
		// 요청을 연결해서 처리해줄 서버 폴더 경로 연결
	}
	
	/* MultipartResolver 설정 */
	@Bean
	public MultipartConfigElement configElement() {
		// MultipartConfigElement
		// 파일 업로드를 처리하는데 사용되는 MultipartConfigElement를 구성하고 반환
		// 파일 업로드를 위한 구성 옵션을 설정하는데 사용
		// 업로드 파일의 최대 크기, 메모리에서 임시 저장 경로를 설정할 수 있음
		// -> 서버 경로 작성 보안상 문제 있을 수 있음
		
		// config.properties 에 적어둘 거임
		
		MultipartConfigFactory factory = new MultipartConfigFactory();
		
		factory.setFileSizeThreshold(DataSize.ofBytes(fileSizeThreshold));
		
		factory.setMaxFileSize(DataSize.ofBytes(maxFileSize));
		
		factory.setMaxRequestSize(DataSize.ofBytes(maxRequestSize));
		
		factory.setLocation(location);
		
		return factory.createMultipartConfig();
	}
	
	// MultipartResolver 객체를 Bean 으로 추가함으로써
	// -> 추가 후 Spring 이 위에서 만든 MultipartConfig 자동으로 이용할 거임
	@Bean
	public MultipartResolver multipartResolver() {
		// MultipartResolver : MultipartFile 을 처리해주는 해결사
		// MultipartResolver는 클라이언트로부터 받은 멀티파트 요청을 처리하고,
		// 이 중에서 업로드된 파일을 추출하여 MultipartFile 객체로 제공하는 역할
		
		StandardServletMultipartResolver multipartResolver
			= new StandardServletMultipartResolver();
		
		return multipartResolver;
	}
	
	
	
}
