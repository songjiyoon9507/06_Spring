package edu.kh.project.common.aop;

import org.aspectj.lang.annotation.Pointcut;

// Pointcut 모아두는 클래스
// Bundle 묶음
// Pointcut : 실제 advice 가 적용될 지점
public class PointcutBundle {

	// 작성하기 어려운 Pointcut 을 미리 작성해놓고
	// 필요한 곳에서 클래스명.메서드명() 으로 호출해서 사용 가능
	
	// ex) @After("execution(* edu.kh.project..*Controller*.*(..))")
	// ==> @After("PointcutBundle.controllerPointCut()")
	
	@Pointcut("execution(* edu.kh.project..*Controller*.*(..))")
	public void controllerPointCut() {}
	
	@Pointcut("execution(* edu.kh.project..*ServiceImpl*.*(..))")
	public void serviceImplPointCut() {}
}
