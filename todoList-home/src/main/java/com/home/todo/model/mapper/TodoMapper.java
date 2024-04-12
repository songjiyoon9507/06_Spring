package com.home.todo.model.mapper;

import org.apache.ibatis.annotations.Mapper;

/* @Mapper
 * - Mybatis 에서 제공하는 어노테이션
 * - 해당 어노테이션이 작성된 인터페이스는
 *   namespace 에 해당 인터페이스가 작성된
 *   mapper.xml 파일과 연결되어 SQL 호출/수행/결과 반환 가능
 * 
 * - Mybatis 에서 제공하는 Mapper 상속 객체가 Bean 으로 등록됨
 * */

//mapper.xml 과 연결될 준비가 됐다는 어노테이션
//mapper.xml 파일에 namespace 가 Mapper 와 연결되는 걸 찾으면 연결해줌
//mapper.xml 파일에 작성한 SQL 을 Mapper 가 말하는대로 수행 호출 결과 반환 해줌
//interface 는 객체화될 수 없음 @Mapper 작성하면 TodoMapper 가 객체가 되는 게 아님
//Mybatis 에서 제공하는 눈에서 확인되지 않는 TodoMapper 를 상속 받은 객체가 있음
//걔가 Bean 으로 등록되는 거

@Mapper
public interface TodoMapper {

}
