package edu.kh.todo.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.todo.model.dto.Todo;

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

	/* Mapper 의 메서드명 == mapper.xml 파일 내 태그의 id
	 * 
	 * 메서드명과 id 가 같은 태그가 서로 연결됨
	 * */
	
	// interface 는 객체가 될 수 없음
	/* 마이바티스에서 제공하고있는 애가 Bean 으로 등록되는 거 TodoMapper라는 interface를 상속받은 객체가 Bean 으로 등록
	 * TodoMapper 자체가 Bean 이 되는 게 아님
	 * */
	
	/** 할 일 목록 조회
	 * @return todoList
	 */
	List<Todo> selectAll();
	// todo-mapper.xml 에서 수행한 SQL 연결해서 결과값 가져옴

	/** 완료된 할 일 개수 조회
	 * @return completeCount
	 */
	int getCompleteCount();

	/** 할 일 추가
	 * @param todo
	 * @return result
	 */
	int addTodo(Todo todo);

	/** 할 일 상세 조회
	 * @param todoNo
	 * @return todo
	 */
	Todo todoDetail(int todoNo);

	/** 완료 여부 수정
	 * @param todo
	 * @return result
	 */
	int changeComplete(Todo todo);

	/** 할 일 수정
	 * @param todo
	 * @return result
	 */
	int todoUpdate(Todo todo);

	/** 할 일 삭제
	 * @param todoNo
	 * @return result
	 */
	int todoDelete(int todoNo);

	/** 전체 할 일 개수 조회
	 * @return totalCount
	 */
	int getTotalCount();
	

}
