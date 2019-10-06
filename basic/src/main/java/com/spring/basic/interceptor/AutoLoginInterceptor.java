package com.spring.basic.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import com.spring.basic.mvcuser.domain.MvcUser;
import com.spring.basic.mvcuser.service.IMvcUserService;

public class AutoLoginInterceptor extends HandlerInterceptorAdapter implements SessionNames {
	
	@Autowired
	private IMvcUserService userService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		HttpSession session = request.getSession();
		
		//1. 자동 로그인 쿠키가 있는지 여부 확인.
		Cookie loginCookie = WebUtils.getCookie(request, "loginCookie");
		if(loginCookie != null) { //자동로그인을 했다면~
			
			//2. DB에서 쿠키에 저장된 세션아이디값을 가진 회원의 정보를 조회
			MvcUser user = userService.getUserWithSessionId(loginCookie.getValue());
			if(user != null) {
				session.setAttribute(LOGIN, user);
				//최종 로그인 시간 갱신
				userService.updateLastLoginTime(user.getAccount());
			}
		}		
		return true;
	}
	
}






