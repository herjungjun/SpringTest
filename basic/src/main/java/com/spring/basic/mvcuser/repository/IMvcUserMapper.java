package com.spring.basic.mvcuser.repository;

import java.util.Map;

import com.spring.basic.mvcuser.domain.MvcUser;

public interface IMvcUserMapper {

	//회원등록 기능
	void register(MvcUser user);
	
	//중복확인 체크(ID, Email)
	int isDuplicate(Map<String, Object> datas);
	
	//단일 회원정보 조회기능
	MvcUser getUserInfo(String account);
	
	//자동로그인 쿠키값을 DB에 저장하는 기능
	void keepLogin(Map<String, Object> datas);
	
	//쿠키값으로 회원정보 불러오기 기능
	MvcUser getUserWithSessionId(String sessionId);
	
	//최종로그인 시간 갱신
	void updateLastLoginTime(String account);
	
	//카카오로그인한 회원정보 조회
	MvcUser getUserInfoWithKakao(String kakaoAccount);
	
}








