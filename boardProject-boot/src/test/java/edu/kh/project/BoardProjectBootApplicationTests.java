package edu.kh.project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// Spring Boot 어플리케이션을 위한 기본 통합 테스트 클래스 환경 -> 프로젝트가 온전한지 테스트 해줌

@SpringBootTest // Spring Boot 애플리케이션 컨텍스트를 로드하여 통합테스트를 수행함
// -> 애플리케이션의 모든 빈(Bean)을 로드하고,
//    이를 통해 실제 애플리케이션이 실행되는 것과 같은 방식으로 테스트를 진행
class BoardProjectBootApplicationTests {

	@Test
	// JUnit에게 이 메서드가 테스트 메서드임을 알려줌
	void contextLoads() {
		// 애플리케이션 컨텍스트가 제대로 로드되는지 확인하는 역할을 함.
		// 애플리케이션 컨텍스트에 로딩이 실패하면, 이 테스트는 실패함
		// Test 어노테이션이 붙은 건 결과가 성공 혹은 실패밖에 없음
		// 정삭적으로 로드가 되면 이 테스트는 통과가 됨.
		// => Spring Boot 애플리케이션에 기본 설정이 올바른지 확인하기 위해서 자동 생성 돼있는 애
	}

}
