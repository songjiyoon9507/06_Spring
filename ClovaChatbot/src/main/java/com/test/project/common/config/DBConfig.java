package com.test.project.common.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

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
 * DataSource : Connection 생성 + Connection Pool 지원하는 객체를
 *              참조하기 위한 Java 인터페이스
 *              (DriverManager 대안, Java JNDI 기술 이용
 *              이름만 부르면 오는 거)
 * 
 * @Autowired
 * - 등록된 Bean 중에서
 *   타입이 일치하거나, 상속관계에 있는 Bean 을
 *   지정된 필드에 주입
 *   == 의존성 주입(DI, Dependency Injection, IOC 관련 기술)
 * */

@Configuration
@PropertySource("classpath:/config.properties")
public class DBConfig {
	
	// 필드
	
	// org.springframework.context.ApplicationContext;
	@Autowired
	private ApplicationContext applicationContext; // application scope 객체 : 즉,현재 프로젝트
	// DI -> 의존성 주입
	// => Spring 에 의존함
	// @Autowired 로 자동 연결
	
	// request/session.getContext() application 객체 만들어서 대입했었음
	
	// HikariCP 설정
	
	// prefix : 접두사
	// @ConfigurationProperties : @PropertySource와 연결해서 사용되는 애
	// @Bean : Bean 으로 등록 Spring 이 관리
	
	// hikariCP 빠르고 효율 좋음
	
	@Bean
	@ConfigurationProperties(prefix="spring.datasource.hikari")
	public HikariConfig hikariConfig() {
		
		return new HikariConfig();
		// spring.datasource.hikari 값들을 가지고 와서 HikariConfig 만듦
		// spring.datasource.hikari 이거는 config.properties 에서 가져옴
		// spring.datasource.hikari 안에는 DB 정보 Hikari 설정 정보들이 들어있음
		// spring.datasource.hikari 로 시작하는 애들 다 가져와서 Bean 으로 만듦 HikariConfig
	}
	
	// 설정 내용들 + CP HikariDataSource 만듦 DataSource interface (이미 자바단에서 만들어놓음) 에 대입
	// CP Connection Pool
	@Bean
	public DataSource dataSource(HikariConfig config) {
		// 위에서 만든 애가 매개변수로 자동 주입
		// 매개변수 HikariConfig config
		// -> 등록된 Bean 중 HikariConfig 타입의 Bean 자동으로 주입
		
		DataSource dataSource = new HikariDataSource(config);
		return dataSource;
	}
	
	// 위에 까지는 hikariCP 위한 설정
	
	// Mybatis 설정
	
	@Bean
	public SqlSessionFactory sessionFactory(DataSource dataSource) throws Exception {
		// 커넥션 + CP + 마이바티스
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		
		// data source 정보 세팅해줘야함
		// 히카리 데이터 소스를 넣어줌
		sessionFactoryBean.setDataSource(dataSource);
		// 다 담아서 공장 객체 만듦
		
		// mapper.xml(SQL) 파일이 모이는 경로 지정
		// -> Mybatis 코드 수행 시 mapper.xml 을 읽을 수 있음
		// sessionFactoryBean.setMapperLocations("현재프로젝트.자원.어떤파일")
		// 현재 프로젝트 필드에 application scope 선언해둠
		// 이미 Spring 이 관리 하는 Bean 임
		sessionFactoryBean.setMapperLocations(
				applicationContext.getResources("classpath:/mappers/**.xml") );
		
		// 호출하는 애한테 throws Exception
		
		// .xml 로 끝나는 모든 파일
		// board-mapper.xml SQL 문을 불러서 세팅해준다는 뜻
		
		// 해당 패키지 내 모든 클래스의 별칭을 등록 (짧게 부를 수 있도록)
		// - Mybatis 는 특정 클래스 지정 시 패키지명.클래스명을 모두 작성해야함
		// -> 너무 길어서 긴 이름을 짧게 부를 별칭 설정할 수 있음
		
		// setTypeAliasesPackage("패키지") 이용 시
		// 클래스 파일명이 별칭으로 등록
		
		// ex) edu.kh.project.model.dto.Member --> Member (별칭 등록)
		sessionFactoryBean.setTypeAliasesPackage("com.test.project");
		
		// 마이바티스 설정 파일 경로 지정 (mybatis-config.xml)
		sessionFactoryBean.setConfigLocation(
					applicationContext.getResource("classpath:/mybatis-config.xml")
				);
		
		// 설정 내용이 모두 적용된 객체 반환
		return sessionFactoryBean.getObject();
	}
	
	// SqlSessionTemplate : 기본 SQL 실행 + 트랜잭션 처리
	/* Connection + DBCP(커넥션 생성하는 거) + Mybatis + 트랜잭션 제어 처리
	 * Data Base Connection Pool
	 * */
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sessionFactory) {
		return new SqlSessionTemplate(sessionFactory);
	}

	// DataSourceTransactionManager : 트랜잭션 매니저(제어 처리)
	@Bean
	public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
	
}