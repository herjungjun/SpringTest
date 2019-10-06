package com.spring.basic.mvcuser.service;

public interface IOAuthLoginService {

	//소셜로그인 사용시 사용자 정보에 접근하는 Access Token을 발급받는 메서드
	String getAccessToken(String authCode) throws Exception;
	
}
