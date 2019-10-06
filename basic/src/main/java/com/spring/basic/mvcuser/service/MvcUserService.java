package com.spring.basic.mvcuser.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.basic.mvcuser.domain.MvcUser;
import com.spring.basic.mvcuser.repository.IMvcUserMapper;

@Service
public class MvcUserService implements IMvcUserService {
	
	@Inject
	private IMvcUserMapper userMapper;

	@Override
	public void register(MvcUser user) {
		
		String rawPw = user.getPassword();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPw = encoder.encode(rawPw);
		user.setPassword(encodedPw);
		userMapper.register(user);
	}
	
	@Override
	public boolean isDuplicate(String kind, String info) {
		
		System.out.println("중복체크 종류: " + kind);
		System.out.println("중복체크할 데이터: " + info);
		
		Map<String, Object> datas = new HashMap<>();
		datas.put("kind", kind);
		datas.put("info", info);
		int resultNum = userMapper.isDuplicate(datas);
		
		if(resultNum == 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public MvcUser getUserInfo(String account) {
		return userMapper.getUserInfo(account);
	}
	
	@Override
	public void keepLogin(String sessionId, Date limitTime, String account) {
		
		Map<String, Object> datas = new HashMap<>();
		datas.put("sessionId", sessionId);
		datas.put("limitTime", limitTime);
		datas.put("account", account);
		
		userMapper.keepLogin(datas);
	}
	
	@Override
	public MvcUser getUserWithSessionId(String sessionId) {
		return userMapper.getUserWithSessionId(sessionId);
	}
	
	@Override
	public void updateLastLoginTime(String account) {
		userMapper.updateLastLoginTime(account);
	}

	@Override
	public MvcUser getUserInfoWithKakao(String kakaoAccount) {
		return userMapper.getUserInfoWithKakao(kakaoAccount);
	}

}





