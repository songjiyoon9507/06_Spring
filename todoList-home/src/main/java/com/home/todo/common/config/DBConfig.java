package com.home.todo.common.config;

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
 * + 객체로 생성해서 내부 코드를 서버 실행시 모두 수행 (설정 내용 토대로 DB 연결)
 * 
 * @PropertySource("classpath:/config.properties")
 * classpath == src/main/resources
 * config.properties 파일을 읽어와서 DBConfig 클래스에서 사용하겠다는 어노테이션
 * 
 * @PropertySource("경로")
 * - 지정된 경로의 properties 파일 내용을 읽어와 사용
 * - 사용할 properties 파일이 다수일 경우
 *   해당 어노테이션을 연속해서 작성하면 됨
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
 *              (DriverManager 대안, Java JNDI 기술 이용)
 *              
 * @Autowired
 * - 등록된 Bean 중에서 타입이 일치하거나, 상속관계에 있는 Bean 을
 *   지정된 필드에 주입
 *   == 의존성 주입(DI, Dependency Injection, IOC 관련 기술)
 * */

@Configuration // 설정용 클래스임을 명시한 어노테이션
@PropertySource("classpath:/config.properties")
public class DBConfig {

	// 필드
	
	// org.springframework.context.ApplicationContext;
	
	@Autowired // (DI, 의존성 주입)
	private ApplicationContext applicationContext; // application scope 객체 : 즉, 현재 프로젝트
	// 이미 Spring이 ApplicationContext를 Bean 으로 관리하고 있어서 @Autowired 로 연결해주기만 하면 됨
	
	// ************* HikariCP 설정 (2가지) *************
	
	@Bean
	@ConfigurationProperties(prefix="spring.datasource.hikari")
	public HikariConfig hikariConfig() {
		/* @ConfigurationProperties 이 어노테이션은
		 * DBConfig 클래스에 있는 @PropertySource 이 어노테이션과 연계됨
		 * 
		 * config.properties 파일 안에 접두사가 spring.datasource.hikari 인 것들을
		 * 전부 불러와 HikariConfig 객체 생성하겠다는 것
		 * 
		 * config.properties 에 담긴 정보들을 이용해서 Connection 을 만들겠다는 것
		 * 
		 * 객체를 만들어서 반환 Spring 이 관리 @Bean 추가
		 * */
		return new HikariConfig();
	}
	
	// 만들어진 HikariConfig 이용해서 datasource 만들 거
	
	@Bean
	public DataSource dataSource(HikariConfig config) {
		/* HikariConfig 이용해서 만들 거 위에 만들어놓은 HikariConfig가
		 * 자동으로 매개변수로 들어와서 연결됨
		 * 
		 * 매개변수 HikariConfig config
		 * -> 등록된 Bean 중 HikariConfig 타입의 Bean 이 Spring 에 의해서 자동으로 주입
		 * 
		 * config.properties 설정 내용 + HikariCP 로 HikariDataSource 생성
		 * 
		 * DataSource 는 interface 형식으로 상속받은 자식들이 대입하는 형태로 객체화
		 * */
		
		DataSource dataSource = new HikariDataSource(config);
		
		return dataSource;
	}
	
	// ************* Mybatis 설정 *************
	
	// SqlSession -> Mybatis 를 지원하는 객체
	
	@Bean
	public SqlSessionFactory sessionFactory(DataSource dataSource) throws Exception {
		/* Mybatis 를 쓸 수 있게 해주는 객체
		 * Bean 으로 만들려면 DataSource 필요
		 * */
		
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		
		sessionFactoryBean.setDataSource(dataSource);
		
		// mapper.xml(SQL) 파일이 모이는 경로 지정
		// -> Mybatis 코드 수행 시 mapper.xml을 읽을 수 있음
		// sessionFactoryBean.serMapperLocations("현재프로젝트.자원.어떤파일");
		
		// DBConfig 필드 부분에 현재프로젝트를 선언해둘 거
		
		sessionFactoryBean.setMapperLocations(
					applicationContext.getResources("classpath:/mappers/**.xml") );
		// .xml 로 끝나는 모든 파일의 SQL 을 모아서 세팅을 해준다는 거
		
		// 해당 패키지 내 모든 클래스의 별칭을 등록
		// Mybatis 이용할 때 패키지명.클래스명 이렇게 불러야하는데 별칭 등록해서
		// 짧게 불러서 쓰려고 하는 설정
		
		/* Mybatis 는 특정 클래스 지정 시 패키지명.클래스명을 모두 작성해야함
		 * 너무 길어서 짧게 부를 별칭을 설정
		 * 
		 * setTypeAliasesPackage("패키지") 이용 시
		 * 클래스 파일명이 별칭으로 등록됨
		 * 
		 * ex) com.home.todo.model.dto.Todo --> Todo 로만 부를 수 있음
		 * 별칭 등록으로 클래스명으로 짧게 부를 수 있음
		 * */
		sessionFactoryBean.setTypeAliasesPackage("com.home.todo");
		
		
		// mybatis 설정 파일 경로 지정 mybatis-config.xml 파일에 setting 만 해두고 연결 X
		// sessionFactoryBean 에 연결
		sessionFactoryBean.setConfigLocation(
					applicationContext.getResource("classpath:/mybatis-config.xml") );
		
		// 설정 내용이 모두 적용된 객체 반환
		return sessionFactoryBean.getObject();
		
	}
	
	// SqlSessionTemplate : Connection + DBCP + Mybatis + 트랜잭션 제어 처리
	
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
