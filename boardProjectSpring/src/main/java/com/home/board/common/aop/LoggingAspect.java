package com.home.board.common.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.home.board.member.model.dto.Member;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

	
	
	@Before("PointcutBundle.controllerPointCut()")
	public void beforeController(JoinPoint jp) {
		
		// AOP가 적용된 클래스 이름 얻어오기
		String className = jp.getTarget().getClass().getSimpleName();
		
		// 실행된 컨트롤러 메서드 이름을 얻어오기
		String methodName = jp.getSignature().getName() + "()";
		
		// 요청한 클라이언트 ip 얻어오기
		// 요청한 클라이언트의 HttpServletRequest 객체 얻어오기
		HttpServletRequest req = 
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		// Holder 는 현재 thread 에 연관된 helper 클래스
		// current 현재 요청이랑 연관된 속성들 반환
		// 반환 타입이 requestAttributes -> interface
		// 실제로 구현이 안됨 interface를 상속 받아서 구현한 ServletRequestAttributes 로 캐스팅 한 것
		// -> Http 요청 객체 얻어옴
		
		// 매개변수에 HttpServletRequest 로 얻어올 수 없음 (Controller 가 아님)
		
		// 클라이언트 ip 얻어오기
		String ip = getRemoteAddr(req);
		
		// log 에 보여줄 문자열 조합
		StringBuilder sb = new StringBuilder();
		
		sb.append(String.format("[%s.%s] 요청 / ip : %s", className, methodName, ip));
		
		// 로그인 상태인 경우 (어떤 회원이 요청을 보냈는지)
		if(req.getSession().getAttribute("loginMember") != null) {
			String memberEmail = 
					( (Member)req.getSession().getAttribute("loginMember") ).getMemberEmail();
			
			sb.append(String.format(", 요청 회원 : %s", memberEmail));
		}
		
		log.info(sb.toString());
		
	}	
	
	
	@Around("PointcutBundle.serviceImplPointCut()")
	public Object aroundServiceImpl(ProceedingJoinPoint pjp) throws Throwable {
		
		// @Before 부분
		
		// 클래스명
		String className = pjp.getTarget().getClass().getSimpleName();
		
		// 메서드명
		String methodName = pjp.getSignature().getName() + "()";
		
		log.info("========== {}.{} 서비스 호출 =========", className, methodName);
		
		// 파라미터 .getArgs() 매개변수 얻어오는 거
		log.info("Parameter : {}", Arrays.toString(pjp.getArgs()));
		
		// 서비스 코드 실행 시 시간 기록 (대상 메서드가 실행되기 전)
		long startMs = System.currentTimeMillis();
		
		Object obj = pjp.proceed(); // 기준으로 전 후 나뉨
		
		// @After 부분
		long endMs = System.currentTimeMillis();
		
		log.info("Running Time : {}ms", endMs - startMs);
		
		log.info("=============================================================");
		
		return obj;
	}	
	
	
	@AfterThrowing(pointcut = "@annotation(org.springframework.transaction.annotation.Transactional)",
			   throwing = "ex")
	public void transactionRollback(JoinPoint jp, Throwable ex) {
		// 트랜잭셔널에서 ex 로 던진 걸 매개변수로 받아옴
		// 메서드 정보 jp
		// 발생한 예외 객체 ex
		log.info("*** 트랜잭션이 롤백됨 {} ***", jp.getSignature().getName());
		log.error("[롤백 원인] : {}", ex.getMessage());
	}
	
	
	private String getRemoteAddr(HttpServletRequest request) {
		
		String ip = null;
		
		ip = request.getHeader("X-Forwarded-For");
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-RealIP");
		}
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("REMOTE_ADDR");
		}
		
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		
		return ip;
		
	}
}
