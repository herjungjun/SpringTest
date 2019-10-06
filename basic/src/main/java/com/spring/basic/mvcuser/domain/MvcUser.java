package com.spring.basic.mvcuser.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MvcUser {

	private String account;
	private String password;
	private String name;
	private String email;
	private String auth;
	private Date regDate;
	private Date lastLoginAt;	
	private String kakaoAccount;
	
}
