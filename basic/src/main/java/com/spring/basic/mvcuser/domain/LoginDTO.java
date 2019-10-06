package com.spring.basic.mvcuser.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class LoginDTO {

	private String account;
	private String password;
	private boolean isAutoLogin; //자동로그인 체크 여부
	
}






