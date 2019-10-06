package com.spring.basic.test.freeboard;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.spring.basic.commons.paging.Page;
import com.spring.basic.freeboard.domain.FreeBoard;
import com.spring.basic.freeboard.repository.IFreeBoardMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {"classpath:/spring/mvc-config.xml"})
public class FreeBoardMapperTest {

	//	@Inject
	@Autowired
	private IFreeBoardMapper mapper;

	//글 작성 테스트
	@Test
	public void createTest() {

		for (int i = 1; i <= 160; i++) {

			FreeBoard article = new FreeBoard();
			article.setTitle("메롱메롱??");
			article.setWriter("까치까치");
			article.setContent("까악까악");
			mapper.create(article);
		}

		System.out.println("게시글 등록 완료!");

	}
	
	@Test
	public void updateTest() {
		
		FreeBoard article = new FreeBoard();
		article.setTitle("수정된 제목이야~");
		article.setContent("수정수정 수정ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ");
		article.setBoardId(100);
		mapper.update(article);
		System.out.println("게시글 수정 완료!");
	}
	
	@Test
	public void deleteTest() {
		
		mapper.delete(150);
		System.out.println("게시글 삭제 완료!");
	}
	
	@Test 
	public void selectOneTest() {
		FreeBoard article = mapper.selectOne(10);
		System.out.println("==================================");
		System.out.println(article);
		System.out.println("==================================");
	}
	
	@Test
	public void selectAllTest() {
		
//		System.out.println("===============================");
//		List<FreeBoard> articles = mapper.selectAll(new Page());
//		System.out.println("게시물 수: " + articles.size());
//		for (FreeBoard freeBoard : articles) {
//			System.out.println(freeBoard);
//		}
//		System.out.println("===============================");
		
	}
	
	@Test
	public void getFileTest() {
		System.out.println("===============================");
		mapper.getFileNames(345)
		.forEach(fileName -> System.out.println("파일명: "+fileName));
		System.out.println("===============================");
	}

}









