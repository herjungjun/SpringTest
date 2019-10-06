package com.spring.basic.freeboardreply.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class FreeBoardReply {
	
	private Integer replyId;
	private String replyText;
	private String replyWriter;
	private Date regDate;
	private Date updateDate;
	private Integer boardId;

}
