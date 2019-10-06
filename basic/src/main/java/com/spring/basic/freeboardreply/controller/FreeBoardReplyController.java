package com.spring.basic.freeboardreply.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.basic.commons.paging.Page;
import com.spring.basic.commons.paging.PageCreator;
import com.spring.basic.freeboardreply.domain.FreeBoardReply;
import com.spring.basic.freeboardreply.service.IFreeBoardReplyService;

@RestController
public class FreeBoardReplyController {
	
	@Autowired
	private IFreeBoardReplyService replyService;
	
	//댓글 목록조회 요청
	@GetMapping("/reply/{boardId}/{page}")
	public Map<String, Object> replyAll(@PathVariable Integer boardId,
						@PathVariable Integer page) {
		
		Page paging = new Page();
		paging.setPage(page);
		
		Integer replyCnt = replyService.countReplies(boardId);
		PageCreator pc = new PageCreator(paging, replyCnt);
		
		List<FreeBoardReply> replies = replyService.selectAll(boardId, paging);
		
		Map<String, Object> replyMap = new HashMap<>();
		replyMap.put("pc", pc);
		replyMap.put("replies", replies);
		replyMap.put("replyCnt", replyCnt);
		
		return replyMap;
	}
	
	//댓글 쓰기 요청 처리
	@PostMapping("/reply")
	public String replyWrite(@RequestBody FreeBoardReply reply) {
		System.out.println("신규 댓글 정보: " + reply);
		replyService.create(reply);
		return "replyRegSuccess";
	}
	
	//댓글 수정 요청 처리
	@PutMapping("/reply/{replyId}")
	public String replyModify(@PathVariable("replyId") Integer repId
							, @RequestBody FreeBoardReply reply) {
		
		System.out.println("댓글 수정 요청: " + reply);
		
		reply.setReplyId(repId);
		replyService.update(reply);
		
		return "replyModSuccess";
	}
	
	//댓글 삭제 요청 처리
	@DeleteMapping("/reply/{replyId}")
	public String replyDelete(@PathVariable Integer replyId,
								Integer boardId) {
		
		System.out.println("댓글 삭제 요청: " + replyId + "번");		
		replyService.delete(replyId, boardId);		
		return "replyDelSuccess";
	}
	

}










