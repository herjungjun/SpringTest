package com.spring.basic.freeboard.service;

import java.util.List;
import java.util.Map;

import com.spring.basic.commons.paging.Page;
import com.spring.basic.commons.paging.Search;
import com.spring.basic.freeboard.domain.FreeBoard;

public interface IFreeBoardService {
	
	void create(FreeBoard article);
	
	Map<String, Object> selectOne(Integer boardId, Page paging);
	List<String> getFileNames(Integer boardId);
	
	List<FreeBoard> selectAll(Search paging);	
	Integer countArticles(Search paging); 
	
	void update(FreeBoard article);	
	
	//게시물 삭제를 위한 중간처리를 위한 메서드
	void deleteArticle(Integer boardId);
	
}


