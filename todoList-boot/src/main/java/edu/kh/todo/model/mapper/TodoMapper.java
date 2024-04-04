package edu.kh.todo.model.mapper;

import org.apache.ibatis.annotations.Mapper;

/* @Mapper
 * - Mybatis 제공 어노테이션
 * - 해당 어노테이션이 작성된 인터페이스는
 *   namespace 에 해당 인터페이스가 작성된
 *   mapper.xml 파일과 연결되어 SQL 호출/수행/결과
 *   반환 가능
 *   
 * - Mybatis 에서 제공하는 Mapper 상속 객체가 Bean 으로 등록됨
 * */

@Mapper // mapper.xml 과 연결될 준비가 됐다는 뜻 todo-mapper.xml 에 주소 써놓은 거 보고 연결함
public interface TodoMapper {
	// todo-mapper.xml 에서 수행한 SQL 연결해서 결과값 가져옴
	
	// interface 는 객체가 될 수 없음
	/* 마이바티스에서 제공하고있는 애가 Bean 으로 등록되는 거 TodoMapper라는 interface를 상속받은 객체가 Bean 으로 등록
	 * TodoMapper 자체가 Bean 이 되는 게 아님
	 * */
}
