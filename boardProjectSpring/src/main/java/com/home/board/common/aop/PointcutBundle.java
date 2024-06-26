package com.home.board.common.aop;

import org.aspectj.lang.annotation.Pointcut;

public class PointcutBundle {

	@Pointcut("execution(* edu.kh.project..*Controller*.*(..))")
	public void controllerPointCut() {}
    
    @Pointcut("execution(* edu.kh.project..*ServiceImpl*.*(..))")
	public void serviceImplPointCut() {}
}
