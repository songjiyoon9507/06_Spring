package edu.kh.todo.common.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/* @Configuration
 * - 설정용 클래스임 명시
 *  + 객체로 생성해서 내부 코드를 서버 실행시 모두 수행
 * 
 * @PropertySource("경로")
 * - 지정된 경로의 properties 파일 내용을 읽어와 사용
 * - 사용할 properties 파일이 다수일 경우
 *   해당 어노테이션을 연속해서 작성하면 됨
 *   
 * classpath == src/main/resources
 * 
 * @ConfigurationProperties(prefix="spring.datasource.hikari")
 * - @PropertySource 로 읽어온 properties 파일의 내용 중
 *   접두사 (앞부분, prefix)가 일치하는 값만 읽어옴
 *   
 * @Bean
 * - 개발자가 수동으로 생성한 객체의 관리를
 *   스프링에게 넘기는 어노테이션
 *   (Bean 등록)
 *   
 * DataSource : 
 * */

@Configuration
@PropertySource("classpath:/config.properties")
public class DBConfig {

	// HikariCP 설정
	
	// prefix : 접두사
	// @ConfigurationProperties : @PropertySource와 연결해서 사용되는 애
	// @Bean : Bean 으로 등록 Spring 이 관리
	
	@Bean
	@ConfigurationProperties(prefix="spring.datasource.hikari")
	public HikariConfig hikariConfig() {
		
		return new HikariConfig();
	}
	
	// 설정 내용들 + CP HikariDataSource 만듦 DataSource interface 에 대입
	
	@Bean
	public DataSource dataSource(HikariConfig config) {
		// 위에서 만든 애가 매개변수로 자동 주입
		// 매개변수 HikariConfig config
		// -> 등록된 Bean 중 HikariConfig 타입의 Bean 자동으로 주입
		
		DataSource dataSource = new HikariDataSource(config);
		return dataSource;
	}
}
