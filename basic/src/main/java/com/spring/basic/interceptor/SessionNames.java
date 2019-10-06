package com.spring.basic.interceptor;

public interface SessionNames {
	
	//인터페이스에 변수를 선언하면 자동으로 public static final이 붙습니다.
	String LOGIN = "loginUser";
	String ATTEMPTED = "attemptedLocation";
	long LIMIT_TIME = 60 * 60 * 24 * 90; //90일
	
}
