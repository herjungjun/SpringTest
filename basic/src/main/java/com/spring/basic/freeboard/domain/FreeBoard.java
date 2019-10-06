package com.spring.basic.freeboard.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FreeBoard {
	
	//camel case : JAVA의 이름 규칙
	//snake case : SQL, Python, 통신처리 쪽 이름 규칙 (board_id)
	private Integer boardId;
	private String title;
	private String content;
	private String writer;
	private Date regDate;
	private Date updateDate;
	private Integer viewCnt;
	private Integer replyCnt;
	
	//첨부파일들의 이름목록을 저장할 필드
	private String[] files;
		
}











