package com.spring.basic.freeboard.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.basic.commons.paging.Page;
import com.spring.basic.commons.paging.Search;
import com.spring.basic.freeboard.domain.FreeBoard;
import com.spring.basic.freeboard.repository.IFreeBoardMapper;
import com.spring.basic.freeboardreply.repository.IFreeBoardReplyMapper;

@Service
public class FreeBoardService implements IFreeBoardService {

	@Autowired
	private IFreeBoardReplyMapper replyMapper;
	@Autowired
	private IFreeBoardMapper boardMapper;
	
	@Transactional
	@Override
	public void deleteArticle(Integer boardId) {
		
		//트랜잭션 처리
		replyMapper.deleteAll(boardId);
		boardMapper.deleteFileNames(boardId);
		boardMapper.delete(boardId);
	}

	@Transactional
	@Override
	public void create(FreeBoard article) {
		boardMapper.create(article);
		
		if(article.getFiles() != null) {		
			for (String fileName : article.getFiles()) {
				boardMapper.addFile(fileName);			
			}
		}
		
	}

	@Transactional
	@Override
	public Map<String, Object> selectOne(Integer boardId, Page paging) {
		
		//컨트롤러에게 전달하는 맵데이터(원본글데이터 + 댓글목록데이터)
		Map<String, Object> returnDatas = new HashMap<>();
		//매퍼에게 전달하는 맵데이터(원본글번호 + 페이지정보)
		Map<String, Object> inputDatas = new HashMap<>();	
		
		inputDatas.put("boardId", boardId);
		inputDatas.put("page", paging);
		
		returnDatas.put("article", boardMapper.selectOne(boardId));		
		returnDatas.put("replies", replyMapper.selectAll(inputDatas));
		
		//조회수 상승처리
		boardMapper.updateViewCnt(boardId);
		
		return returnDatas;
	}
	
	@Override
	public List<String> getFileNames(Integer boardId) {
		return boardMapper.getFileNames(boardId);
	}

	@Override
	public List<FreeBoard> selectAll(Search paging) {
		return boardMapper.selectAll(paging);
	}

	@Override
	public Integer countArticles(Search paging) {
		return boardMapper.countArticles(paging);
	}

	@Override
	public void update(FreeBoard article) {
		boardMapper.update(article);
	}

}









