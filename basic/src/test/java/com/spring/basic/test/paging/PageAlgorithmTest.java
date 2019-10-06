package com.spring.basic.test.paging;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.spring.basic.commons.paging.Page;
import com.spring.basic.freeboard.repository.IFreeBoardMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {"classpath:/spring/mvc-config.xml"})
public class PageAlgorithmTest {
	
	/*
	 *** 페이징 알고리즘 만들기
	 
	 # 1. 사용자가 보게 될 페이지 화면
	 - 한 화면에 페이지를 10개씩 끊어서 보여준다면?
	 ex) 1 2 3 4 ... 9 10 [다음] // [이전] 31 32 33 ... 39 40 [다음]
	 
	 - 만약에 총 게시물의 수가 67개라면??
	 ex) 1 2 3 4 5 6 7 //이전, 다음 비활성화
	 
	 - 총 게시물 수가 142개이고 현재 12페이지에 사용자가 머물러있다면??
	 ex) [이전] 11 12 13 14 15
	 
	 # 2. 우선 총 게시물의 수를 조회해야 합니다.
	 - 총 게시물 수는 DB로부터 수를 조회하는 SQL을 작성.
	 
	 # 3. 사용자가 현재 위치한 페이지를 기준으로 
	      끝 페이지 번호를 계산하는 로직 작성
	 
	 - 만약 현재 사용자가 보고 있는 페이지가 3페이지고 한 화면에 
	   보여줄 페이지가 10페이지씩이라면??
	   -> 끝 페이지 번호는 ?? 10페이지
	 - 만약 현재 페이지가 37페이지고 한 화면에 보여줄 페이지가
	   20페이지씩이라면??
	   -> 끝 페이지 번호는 ?? 40페이지
	  
	 - 공식: Math.ceil(현재 위치한 페이지 번호 / 한 화면당 보여질 페이지의 수) 
	  			* 한 화면당 보여질 페이지의 수
	 
	 # 4. 시작 페이지 번호 계산하기
	 - 현재 위치한 페이지가 15페이지이고, 한 화면에 보여줄 페이지가 10페이지씩이라면??
	   -> 시작페이지 번호는?? 11페이지
	 
	 - 현재 위치한 페이지가 73페이지이고, 한 화면에 20페이지씩 보여준다면??
	   -> 시작페이지 번호는?? 61페이지
	 
	 - 공식: (끝 페이지 번호 - 한 화면에 보여줄 페이지의 수) + 1
	 
	 # 5. 끝 페이지 보정
	 
	 - 총 게시물 수가 324개이고 한 페이지당 10개의 게시물을 보여준다.
	 - 그리고 현재 이사람은 31페이지를 보고 있다.
	 - 그리고 한 화면당 10개씩의 페이지를 보여주고 있다.
	 - 그렇다면 위 공식에 의한 끝페이지는 어떻게 계산되는가?? 40페이지
	 - 하지만 실제 끝페이지는 몇번이어야 하는가?? 33페이지
	 
	 # 5-1. 이전버튼 활성 여부 설정
	 - 언제 이전버튼을 비활성화 할 것인가?? 시작페이지가 1인 부분에서 비활성.
	 - 공식: 시작페이지번호가 1로 구해진 시점에서 비활성 처리. 나머지는 활성.
	 
	 # 5-2. 다음버튼 활성 여부 설정
	 - 언제 다음버튼을 비활성화 할 것인가?? 
	 - 공식: 보정전 끝페이지번호 x 한 페이지에 들어갈 게시물 수 >= 총 게시물수 -> 비활성
	 
	 # 5-3: 끝 페이지 값 보정
	 - 다음 버튼이 비활성화 되었을 때 총 게시물 수에 맞춰 끝페이지번호를
	   재보정 한다.
	 - 공식: Math.ceil(총 게시물의 수 / 한 페이지에 보여줄 게시물 수)
	 
	 */
	
	@Autowired
	private IFreeBoardMapper mapper;
	
	@Test
	public void pagingTest() {
		
		//총 게시물 수를 구하는 테스트
		System.out.println("=======================");
//		System.out.println("# 총 게시물 수: " + mapper.countArticles() + "개");
		System.out.println("=======================");
		
		//끝 페이지 번호 계산 테스트
		Page paging = new Page();
		paging.setPage(13);
		int displayPage = 10;
		
		int endPage = (int)Math.ceil(paging.getPage() / (double)displayPage) * displayPage;
		
		//시작 페이지 번호 계산 테스트
		int beginPage = (endPage - displayPage) + 1;
		System.out.println("시작 페이지 번호: " + beginPage + "번");
		System.out.println("끝 페이지 번호: " + endPage + "번");
		
		//이전 버튼 활성화 여부 계산
		boolean prev = (beginPage == 1) ? false : true;
		System.out.println("이전 버튼 활성?? " + prev);
		
		//다음 버튼 활성화 여부 계산
//		boolean next = (endPage * paging.getCountPerPage() >= mapper.countArticles()) ? false : true;
//		System.out.println("다음 버튼 활성?? " + next);
//		
//		//끝 페이지 재보정
//		if(!next) {
//			endPage = (int)Math.ceil((double)mapper.countArticles() / paging.getCountPerPage());
//		}
//		System.out.println("보정 후 끝 페이지 번호: " + endPage + "번");
	}

}












