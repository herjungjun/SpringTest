package com.spring.basic.freeboard.repository;

import java.util.List;
import java.util.Map;

import com.spring.basic.commons.paging.Search;
import com.spring.basic.freeboard.domain.FreeBoard;

public interface IFreeBoardMapper {

	//CRUD관련 추상 메서드 선언.
	//1-a. create: 글 쓰기 기능
	void create(FreeBoard article);
	//1-b. addFile: 파일 첨부 기능
	void addFile(String fileName);
	
	//2. read : 조회 기능
	//   - a: single row -> 특정 게시글 조회 
	//   - b: multi row -> 게시글 목록 조회
	FreeBoard selectOne(Integer boardId);
	List<String> getFileNames(Integer boardId);
	
	List<FreeBoard> selectAll(Search paging);
	
	Integer countArticles(Search paging); //총 게시물 수 조회
	
	//3. update: 글 수정 기능
	void update(FreeBoard article);
	
	//댓글 개수 상승, 하락 처리
	void updateReplyCnt(Map<String, Object> datas);
	//조회수 상승처리
	void updateViewCnt(Integer boardId);
	
	//4. delete: 글 삭제 기능
	void delete(Integer boardId);
	void deleteFileNames(Integer boardId);
	
}









