package com.spring.basic.freeboardreply.service;

import java.util.List;

import com.spring.basic.commons.paging.Page;
import com.spring.basic.freeboardreply.domain.FreeBoardReply;

public interface IFreeBoardReplyService {

	//댓글 쓰기 기능
	void create(FreeBoardReply reply);

	//댓글 수정 기능
	void update(FreeBoardReply reply);

	//댓글 삭제 기능
	void delete(Integer replyId, Integer boardId);

	//원본 게시물 삭제시 해당 게시물의 댓글 전체 삭제 기능
	void deleteAll(Integer boardId);

	//댓글 목록 조회 기능
	List<FreeBoardReply> selectAll(Integer boardId, Page page);

	//특정 게시물의 총 댓글 수 조회 기능
	Integer countReplies(Integer boardId);	

}







