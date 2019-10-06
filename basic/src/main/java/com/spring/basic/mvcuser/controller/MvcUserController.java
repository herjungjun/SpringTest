package com.spring.basic.mvcuser.controller;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.spring.basic.interceptor.SessionNames;
import com.spring.basic.mvcuser.domain.LoginDTO;
import com.spring.basic.mvcuser.domain.MvcUser;
import com.spring.basic.mvcuser.service.IMvcUserService;
import com.spring.basic.mvcuser.service.KakaoService;

@RestController
public class MvcUserController {
	
	@Autowired
	private IMvcUserService userService;
	
	@Autowired
	private KakaoService kakaoService;
	
	//회원등록 요청처리 테스트 메서드
	@PostMapping("/regTest")
	public String regTest(@RequestBody MvcUser user) {
		System.out.println("param: " + user);
		userService.register(user);
		return "regSuccess";
	}
	
	//데이터(계정, 이메일) 중복체크 요청
	@GetMapping("/check")
	public ResponseEntity<String> check(String info, String kind) {
		
//		System.out.println("중복체크 종류: " + kind);
//		System.out.println("중복체크할 데이터: " + info);
		
		try {
			boolean flag = userService.isDuplicate(kind, info);
			if(flag) { //데이터가 중복데이터임!
				return new ResponseEntity<>("true", HttpStatus.OK);
			} else { //중복데이터가 아님!
				return new ResponseEntity<>("false", HttpStatus.OK);
			}
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	//회원등록 페이지 화면 요청처리 메서드
	@GetMapping("/register")
	public ModelAndView registerGET() {
		return new ModelAndView("user/register");		
	}
	
	//회원등록 요청처리 메서드
	@PostMapping("/register")
	public ModelAndView register(MvcUser regData) {
		
		ModelAndView mv = new ModelAndView();
		userService.register(regData);
		mv.addObject("regResult", "OK");
		mv.setViewName("user/login");
		return mv;		
	}
	
	//로그인 페이지 화면요청처리 메서드
	@GetMapping("/login")
	public ModelAndView login(Integer check, HttpSession session) {
		
		//check => 글번호
		if(check != null) 
			session.setAttribute("check", check);
		
		return new ModelAndView("user/login");
	}
	
	//로그인 검증 요청처리 메서드
	@PostMapping("/loginCheck")
	public ModelAndView loginCheck(LoginDTO loginData,
			HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		
		System.out.println("LoginData: " + loginData);
		ModelAndView mv = new ModelAndView();
		
		MvcUser user = userService.getUserInfo(loginData.getAccount());
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if(user != null) {
			if(encoder.matches(loginData.getPassword(), user.getPassword())) {
				
				//최종 로그인 시간 갱신
				userService.updateLastLoginTime(user.getAccount());
				
				//자동로그인 DB처리
				long expiredDate = System.currentTimeMillis() + SessionNames.LIMIT_TIME * 1000;
				Date limitDate = new Date(expiredDate);
				userService.keepLogin(session.getId(), limitDate, loginData.getAccount());
				
				mv.addObject("user", user);
			} else {
				mv.addObject("loginResult", "pwFail");
			}			
		} else {
			mv.addObject("loginResult", "idFail");
		}
		mv.setViewName("user/loginCheck");
		return mv;
		
	}
	
	//로그아웃 요청처리
	@GetMapping("/logout")
	public ModelAndView logout(HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		MvcUser user = (MvcUser)session.getAttribute(SessionNames.LOGIN);
		
		if(user != null) {
			
			String loginMethod = (String)session.getAttribute("loginMethod");
			if(loginMethod.equals("kakao")) { //만약 카카오 로그아웃이라면
				kakaoService.kakaoLogout((String)session.getAttribute("access_token"));
			}
			
			session.removeAttribute(SessionNames.LOGIN);
			session.invalidate(); //세션 객체 통채로 무효화
			
			//로그아웃 시 자동로그인 쿠키 삭제 및 해당 회원 정보에서 session_id제거
			/*
			 1. loginCookie를 읽어온 뒤 해당 쿠키가 존재하는지 여부 확인
			 2. 쿠키가 존재한다면 쿠키의 수명을 0초로 다시 설정한 후(setMaxAge사용)
			 3. 응답객체를 통해 로컬에 0초짜리 쿠키 재전송 -> 쿠키 삭제
			 4. service를 통해 keepLogin을 호출하여 DB컬럼 레코드 재설정
			   (session_id -> "none", limit_time -> 현재시간으로)
			 */
			Cookie loginCookie = WebUtils.getCookie(request, "loginCookie");
			if(loginCookie != null) {
				loginCookie.setMaxAge(0);
				response.addCookie(loginCookie);
				userService.keepLogin("none", new Date(), user.getAccount());
			}			
		}		
		return new ModelAndView("redirect:/");
	}
	
}












