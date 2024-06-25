package edu.kh.project.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

// JUnit : Java 개발 표준 테스트 프레임워크
// 클래스 개개인마다 단위 테스트 가능 / 전체 테스트도 가능
// -> 코드의 정상 동작을 확인하기 위해서 테스트를 작성하고 실행함

/*
	주요 어노테이션
	
	@Test: 이 메서드가 테스트 메서드임을 나타냄
	@BeforeEach: 각 테스트 메서드가 실행되기 전에 실행되는 메서드를 정의함
	@AfterEach: 각 테스트 메서드가 실행된 후에 실행되는 메서드를 정의함
	@BeforeAll: 모든 테스트 메서드가 실행되기 전에 한 번 실행되는 메서드를 정의함
	@AfterAll: 모든 테스트 메서드가 실행된 후에 한 번 실행되는 메서드를 정의함
	
	주요 메서드 (assert : 단언하다/주장하다, (프로그래밍에서) 가정하다) => a가 b임을 가정한다.
	-> JUnit 제공 메서드
	assertEquals : 두 값이 같은지 확인, 같지 않으면 테스트가 실패함
	assertTrue : 조건이 참인지 확인, 거짓이면 테스트가 실패함
	assertFalse : 조건이 거짓인지 확인, 참이면 테스트가 실패함
	assertNotNull : 객체가 null이 아닌지 확인, null이면 테스트가 실패함
	assertThrows : 특정 예외가 발생하는지 확인, 예외가 발생하지 않으면 테스트가 실패함
*/

@Slf4j
public class CalculatorTest {

	// Calculator 메서드 하나하나 테스트해볼 거임
	
	// 인스턴스 생성
	private Calculator calculator = new Calculator();
	
	// BoforeAll 사용해보기 Slf4j 이용
	@BeforeAll
	public static void start() {
		log.info("테스트 시작");
	}
	
	@Test
	public void testAdd() {
		// assertEquals(예상값, 실제값);
		// Calculator 호출해야함 -> 실제값에 넣어주기 위해
		assertEquals(5, calculator.add(2, 3));
		// => 예상값이 실제값에 적힌 메서드 실행 후 같은 값이면 성공, 다르면 실패
	}
	
	@Test
	public void testSubtract() {
		assertEquals(1, calculator.subtract(3, 2));
	}
	
	@Test
	public void testMultiply() {
		assertEquals(6, calculator.multiply(2, 3));
	}
	
	@Test
	public void testDivide() {
		assertEquals(3, calculator.divide(6, 2));
	}
	
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
		log.info("테스트 모두 완료");
	}

	// JUnit5 에서 @BeforAll / @AfterAll 메서드는 기본적으로 static 메서드여야함.
	// 어노테이션 작성 안하면 수행 안됨. Test, BeforeAll, AfterAll 등등 모두 다
	// test 클래스에 있지만 어노테이션 붙이지 않으면 수행 안됨.
}
