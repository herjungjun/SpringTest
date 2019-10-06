package com.spring.basic.commons.paging;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PageCreator {

	//페이지번호 정보와 한 페이지당 들어갈 게시물 수의 정보를 갖는 객체
	private Page paging;
	private Integer articleTotalCount;//게시판의 총 게시물 수
	private Integer beginPage; //시작 페이지 번호
	private Integer endPage; //끝 페이지 번호
	private boolean prev; //이전 버튼 활성화 여부
	private boolean next; //다음 버튼 활성화 여부

	//한 화면에 제한할 페이지의 개수.
	private final Integer displayPageNum = 10;

	public PageCreator(Page paging, Integer articleTotalCount) {
		this.paging = paging;
		this.articleTotalCount = articleTotalCount;

		//보정 전 끝 페이지 구하기
		endPage = (int)Math.ceil(paging.getPage() 
				/ (double)displayPageNum)
				* displayPageNum;

		//시작 페이지 번호 구하기
		beginPage = (endPage - displayPageNum) + 1;

		//현재 시작페이지가 1이라면 이전버튼 활성화 여부를 false로 지정
		prev = (beginPage == 1) ? false : true;

		//마지막 페이지인지 여부 확인 후 다음 버튼 비활성.
		next = (articleTotalCount <= (endPage * paging.getCountPerPage())) ? false : true;

		//재보정 여부 판단하기
		if(!isNext()) {
			//끝 페이지 재보정하기
			endPage = (int)Math.ceil(articleTotalCount / (double)paging.getCountPerPage());
		}

	}

}









