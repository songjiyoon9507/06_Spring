package com.home.board.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

/*
주요 어노테이션

@Test: 이 메서드가 테스트 메서드임을 나타냄
@BeforeEach: 각 테스트 메서드가 실행되기 전에 실행되는 메서드를 정의함
@AfterEach: 각 테스트 메서드가 실행된 후에 실행되는 메서드를 정의함
@BeforeAll: 모든 테스트 메서드가 실행되기 전에 한 번 실행되는 메서드를 정의함
@AfterAll: 모든 테스트 메서드가 실행된 후에 한 번 실행되는 메서드를 정의함

주요 메서드

assertEquals : 두 값이 같은지 확인, 같지 않으면 테스트가 실패함
assertTrue : 조건이 참인지 확인, 거짓이면 테스트가 실패함
assertFalse : 조건이 거짓인지 확인, 참이면 테스트가 실패함
assertNotNull : 객체가 null이 아닌지 확인, null이면 테스트가 실패함
assertThrows : 특정 예외가 발생하는지 확인, 예외가 발생하지 않으면 테스트가 실패함
 */

@Slf4j
public class CalculatorTest {

	// src/main/java 에 있는 Calculator 인스턴스 생성
	private Calculator calculator = new Calculator();
	
	@BeforeAll
	public static void start() {
		log.info("테스트 시작 static 붙여야함 @BeforeAll");
	}
	
	@Test
	public void testAdd() {
		// main/java 에 있는 Calculator 안에 add 메서드 테스트
		// test 하는 메서드에는 @Test 어노테이션 붙여줘야함.
		assertEquals(5, calculator.add(2, 3));
		// 예상한 값이 실제 계산한 값과 같아야 성공
	}

/* main/java 에 적힌 메서드 내용
 
 	public int divide(int a, int b) {
		// 0 으로 나눌 수 없다는 예외 처리
		if(b == 0) {
			// 문법적 오류 IllegalArgumentException
			throw new IllegalArgumentException("0으로 나눌 수 없음");
		}
		
		return a / b;
	}
 */
	// assertThrows 특정 예외가 발생하는지 메서드 이용해보기
	@Test
	public void testDivideByZero() {
		// assertThrows(기대되는 예외 클래스, 예외가 발생할 것으로 예상되는 코드); 발생할 것 같은 예외 클래스
		// 예외가 발생할 것으로 예상되는 코드는 lamda 식으로 많이 씀
		assertThrows(IllegalArgumentException.class,
					() -> calculator.divide(1 ,0) );
		// 특정 예외가 발생할 것을 기대하고 테스트 하는 것
		// 0이 아닌 다른 값 전달 시 예외 발생 x => test 실패함
	}
	
	// test 돌리는 방법
	// src/test/java -> CalculatorTest 클래스 오른쪽 마우스 클릭 후
	// run as -> junit test 클릭
	
	// assertTrue 사용해보기
	@Test
	public void testExam() {
		// assertTrue(테스트할 값);
		assertTrue(calculator.exam());
	}
	
	// AfterAll 사용해보기
	@AfterAll
	public static void end() {
		log.info("테스트 모두 완료 @AfterAll static 꼭 붙여줘야함");
	}
}
