package edu.kh.project.common.config;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.kh.project.common.filter.LoginFilter;

/* 만들어놓은 LoginFilter 클래스가 언제 적용될지 설정 */

@Configuration // 서버가 켜질 때 해당 클래스 내 모든 메서드가 실행됨
public class FilterConfig {

	@Bean // 반환된 객체를 Bean 으로 등록 : LoginFilter 로 타입을 제한함
	public FilterRegistrationBean<LoginFilter> loginFilter() {
		// FilterRegistrationBean : 필터를 Bean 으로 등록하는 객체
		
		FilterRegistrationBean<LoginFilter> filter
			= new FilterRegistrationBean<>();
		// 필터 객체 생성 (아직 껍데기)
		
		// 사용할 필터 객체 추가
		filter.setFilter(new LoginFilter());
		// LoginFilter 는 Bean 등록 안함 이용하려면 new 연산자 통해서 객체 생성
		// LoginFilter setting
		
		// 필터링할 URL 작성
		// /myPage/* : myPage로 시작하는 모든 요청
		String[] filteringURL = {"/myPage/*", "/editBoard/*"};
		
		// 필터가 동작할 URL 세팅
		// Arrays.asList(filteringURL)
		// -> filteringURL 배열을 List로 변환
		filter.setUrlPatterns(Arrays.asList(filteringURL));
		// setUrlPatterns() 매개변수로 collection 객체를 받음
		
		// 필터 이름 지정
		filter.setName("loginFilter");
		
		// 필터 순서 지정
		filter.setOrder(1);
		
		return filter; // 반환된 객체가 필터를 생성해서 Bean 으로 등록
	}
}
