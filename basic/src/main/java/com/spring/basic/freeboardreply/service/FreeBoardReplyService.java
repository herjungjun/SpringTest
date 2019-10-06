package com.spring.basic.freeboardreply.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.basic.commons.paging.Page;
import com.spring.basic.freeboard.repository.IFreeBoardMapper;
import com.spring.basic.freeboardreply.domain.FreeBoardReply;
import com.spring.basic.freeboardreply.repository.IFreeBoardReplyMapper;

@Service
public class FreeBoardReplyService implements IFreeBoardReplyService {

	@Autowired
	private IFreeBoardReplyMapper replyMapper;
	
	@Autowired
	private IFreeBoardMapper boardMapper;
	
	@Transactional
	@Override
	public void create(FreeBoardReply reply) {
		replyMapper.create(reply);
		
		Map<String, Object> datas = new HashMap<>();
		datas.put("count", 1);
		datas.put("boardId", reply.getBoardId());
		
		boardMapper.updateReplyCnt(datas);
	}

	@Override
	public void update(FreeBoardReply reply) {
		replyMapper.update(reply);
	}


	@Transactional
	@Override
	public void delete(Integer replyId, Integer boardId) {
		replyMapper.delete(replyId);
		
		Map<String, Object> datas = new HashMap<>();
		datas.put("count", -1);
		datas.put("boardId", boardId);
		
		boardMapper.updateReplyCnt(datas);
	}

	@Override
	public void deleteAll(Integer boardId) {
		replyMapper.deleteAll(boardId);
	}

	@Override
	public List<FreeBoardReply> selectAll(Integer boardId, Page page) {
		
		Map<String, Object> datas = new HashMap<>();
		datas.put("boardId", boardId);
		datas.put("page", page);
		
		return replyMapper.selectAll(datas);
	}

	@Override
	public Integer countReplies(Integer boardId) {
		return replyMapper.countReplies(boardId);
	}

	

}




