package com.spring.basic.mvcuser.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.spring.basic.commons.util.OAuthValue;
import com.spring.basic.interceptor.SessionNames;
import com.spring.basic.mvcuser.domain.MvcUser;
import com.spring.basic.mvcuser.service.IMvcUserService;
import com.spring.basic.mvcuser.service.KakaoService;

@RestController
public class KakaoController {	
	
	@Autowired
	private KakaoService kakaoService;
	
	@Autowired
	private IMvcUserService userService;
	
	//테스트페이지 화면요청 메서드
	@GetMapping("/kakao-test")
	public ModelAndView kakaoTest() {
		
		ModelAndView mv = new ModelAndView();
		
		mv.addObject("app_key", OAuthValue.KAKAO_APP_KEY);
		mv.addObject("redirect_uri", OAuthValue.KAKAO_REDIRECT_PATH);
		
		mv.setViewName("test/kakao-test");
		return mv;
	}
	
	//카카오 서버가 보내준 리다이렉션에 대해서 처리할 요청 메서드
	//인증코드를 받아 사용자 정보에 접근할 수 있는 Access Token을 발급받는 과정
	@GetMapping("/auth/kakao")
	public ModelAndView kakaoLogin(@RequestParam String code
			, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		
		//System.out.println("인증코드: " + code);
		String accessToken = kakaoService.getAccessToken(code);
		
		//사용자 정보 요청(프로필, 닉네임, 이메일, 성별)		
	    Map<String, Object> userInfo = kakaoService.getKakaoUserInfo(accessToken);
	    
	    //사용자 정보가 반환되었다면 로그인 처리.
	    if(userInfo != null) {
	    	
	    	//카카오 이메일을 뽑아봄.
	    	String kakaoAccount = (String)userInfo.get("email");
	    	MvcUser user = userService.getUserInfoWithKakao(kakaoAccount);
	    	if(user == null) {
	    		//카카오 로그인이 처음인 회원
	    		MvcUser newUser = new MvcUser();
	    		newUser.setAccount(kakaoAccount);
	    		newUser.setPassword("kakao1234");
	    		newUser.setEmail(kakaoAccount);
	    		newUser.setName((String)userInfo.get("nickname"));
	    		newUser.setAuth("common");
	    		newUser.setKakaoAccount(kakaoAccount);
	    		userService.register(newUser);
	    	} 
	    	//카카오 로그인 정보가 있는 회원
	    	session.setAttribute(SessionNames.LOGIN, user);
	    	session.setAttribute("access_token", accessToken);
	    	session.setAttribute("loginMethod", "kakao");	    	
	    	
	    }		
		mv.setViewName("redirect:/");
		return mv;
	}
	
	//카카오 로그아웃 요청처리
	@GetMapping("/kakao/logout")
	public ModelAndView kakaoLogout(HttpSession session) throws Exception {
		
		ModelAndView mv = new ModelAndView();
		
		//카카오 서버 로그아웃
		kakaoService.kakaoLogout((String)session.getAttribute("access_token"));
		
		//우리 어플리케이션 로그아웃
		session.removeAttribute(SessionNames.LOGIN);
		session.invalidate();
		
		mv.setViewName("redirect:/kakao-test");
		return mv;
		
	}

}










