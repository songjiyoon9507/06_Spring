package com.home.board.test;

public class Calculator {
	
	public int add(int a, int b) {
		return a+b;
	}
	
	public int subtract(int a, int b) {
		return a-b;
	}
	
	public int multiply(int a, int b) {
		return a*b;
	}
	
	public int divide(int a, int b) {
		// 0 으로 나눌 수 없다는 예외 처리
		if(b == 0) {
			// 문법적 오류 IllegalArgumentException
			throw new IllegalArgumentException("0으로 나눌 수 없음");
		}
		
		return a / b;
	}
    
    public boolean exam() {
		return 1 == 1;
	}
    
}
